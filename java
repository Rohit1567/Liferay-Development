package sbi.feedback.form.portlet;


import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.liferay.petra.string.CharPool;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.RestrictionsFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.PortletResponseUtil;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ContentTypes;
import com.liferay.portal.kernel.util.HtmlUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.sbi.core.service.model.FeedbackForm;
import com.sbi.core.service.model.SgBranchMaster;
import com.sbi.core.service.model.SgExecutiveMaster;
import com.sbi.core.service.model.SgFeedbackForm;
import com.sbi.core.service.service.FeedbackFormLocalServiceUtil;
import com.sbi.core.service.service.SgBranchMasterLocalServiceUtil;
import com.sbi.core.service.service.SgExecutiveMasterLocalServiceUtil;
import com.sbi.core.service.service.SgFeedbackFormLocalServiceUtil;
import com.sbi.util.api.SBIUtil;

@Component(
	immediate = true,
	property = {
        "com.liferay.portlet.display-category=SBI-Internet",
        "com.liferay.portlet.instanceable=true",
        "javax.portlet.security-role-ref=power-user,user",
        "javax.portlet.init-param.view-template=/feedbackReport.jsp",
        "javax.portlet.display-name=Singapore BranchExecutive Feedback Report"
    },
    service = Portlet.class
)

public class SbiSgFeedbackReportPortlet extends MVCPortlet {
	
	private Log log = LogFactoryUtil.getLog(SbiSgFeedbackReportPortlet.class);
	
	@Reference
	SBIUtil sbiUtil;
	
	@Override
	public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
			throws IOException, PortletException {
		log.info("Inside serveResource");
		ThemeDisplay themeDisplay = (ThemeDisplay) resourceRequest.getAttribute(WebKeys.THEME_DISPLAY);
		try {

			String sFromDate = HtmlUtil.escape(ParamUtil.getString(resourceRequest, "fromDate"));
			String sToDate = HtmlUtil.escape(ParamUtil.getString(resourceRequest, "toDate"));
			log.info("sFromDate : " + sFromDate);
			log.info("sToDate : " + sToDate);

//			if (Validator.isNotNull(sFromDate)) {
//				log.info("from date : " + sFromDate);
//				DateFormat srcDf = new SimpleDateFormat("dd-MMM-yyyy");
//				Date date = srcDf.parse(sFromDate);
//				DateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
//				sFromDate = destDf.format(date);
//				log.info("from dateeee: " + sFromDate);
//			}
//			if (Validator.isNotNull(sToDate)) {
//				log.info("from date : " + sToDate);
//				DateFormat srcDf = new SimpleDateFormat("dd-MMM-yyyy");
//				Date date = srcDf.parse(sToDate);
//				DateFormat destDf = new SimpleDateFormat("dd/MM/yyyy");
//				sToDate = destDf.format(date);
//				log.info("from dateeee: " + sToDate);
//			}

//			String reportParams = "pGroupID=" + themeDisplay.getScopeGroupId() + "&pFromDate=" + sFromDate + "&pToDate="
//					+ sToDate;
//			byte[] bytesArrayForGenerateReport = sbiUtil.getJasperBytesArrayForPortal(
//					Constant.JASPER_FEEDBACK_GENERATE_REPORT_PATH, Constant.JASPER_FEEDBACK_GENERATE_REPORT_NAME,
//					Constant.JASPER_FEEDBACK_GENERATE_REPORT_EXTENSION, reportParams);
//			PortletResponseUtil.sendFile(resourceRequest, resourceResponse, "FeedBackDetailsReport.xlsx",
//					bytesArrayForGenerateReport, ContentTypes.APPLICATION_VND_MS_EXCEL);

			DynamicQuery dynamicQuery = SgFeedbackFormLocalServiceUtil.dynamicQuery();
			Date fromDate = null;
			Date toDate = null;
			if (Validator.isNotNull(sFromDate)) {
				fromDate = new SimpleDateFormat("dd-MMM-yyyy").parse(sFromDate);
			}
			if (Validator.isNotNull(sToDate)) {
				toDate = new SimpleDateFormat("dd-MMM-yyyy").parse(sToDate);
				Calendar toCal = Calendar.getInstance();
				toCal.setTime(toDate);
				toCal.set(Calendar.MILLISECOND, 59);
				toCal.set(Calendar.SECOND, 59);
				toCal.set(Calendar.MINUTE, 59);
				toCal.set(Calendar.HOUR, 23);
				toDate = toCal.getTime();
			}
			log.info("fromDate : " + fromDate);
			log.info("toDate : " + toDate);
			long gid = themeDisplay.getSiteGroupId();
			if (Validator.isNotNull(fromDate) && Validator.isNotNull(toDate)) {
				dynamicQuery.add(RestrictionsFactoryUtil.between("createDate", fromDate, toDate));
				
			} else if (Validator.isNotNull(fromDate) && Validator.isNull(toDate)) {
				dynamicQuery.add(RestrictionsFactoryUtil.ge("createDate", fromDate));
				
			} else if (Validator.isNull(fromDate) && Validator.isNotNull(toDate)) {
				dynamicQuery.add(RestrictionsFactoryUtil.le("createDate", toDate));
				
			}

			StringBundler sb = new StringBundler();
			for (String columnName : COLUMN_NAMES) {
				sb.append(getCSVFormattedValue(columnName));
				sb.append(StringPool.COMMA);
				sb.append(StringPool.COMMA);
				sb.setIndex(sb.index() - 1);
			}
			sb.append(CharPool.NEW_LINE);
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
			List<SgFeedbackForm> feedbackform = SgFeedbackFormLocalServiceUtil.dynamicQuery(dynamicQuery);
			log.info("----> feedbackform : " + feedbackform.size());
			String groupId = "1972585";
			log.info("GroupID__SITE_IS = " + groupId);
			for (SgFeedbackForm feedback : feedbackform) {
				String employeeName = FindemployeeName(feedback.getEmployeeId());
				String branchName = FindbranchName(feedback.getBranchId());
				log.info("Data is>>"+ employeeName+">>"+branchName);
				sb.append(getCSVFormattedValue(dateFormat.format(feedback.getCreateDate())));
				sb.append(StringPool.COMMA);
				sb.append(getCSVFormattedValue(branchName));
				sb.append(StringPool.COMMA);
				sb.append(getCSVFormattedValue(employeeName));
				sb.append(StringPool.COMMA);
				sb.append(getCSVFormattedValue(feedback.getRateOurServ()));
				sb.append(StringPool.COMMA);
				sb.append(getCSVFormattedValue(feedback.getExecutiveRating()));
				sb.append(StringPool.COMMA);
				sb.append(getCSVFormattedValue(feedback.getCustComment()));
				sb.append(StringPool.COMMA);
				sb.append(getCSVFormattedValue(feedback.getCustName()));
				sb.append(StringPool.COMMA);
				sb.append(getCSVFormattedValue(feedback.getResponseType()));
				sb.append(StringPool.COMMA);
				sb.append(getCSVFormattedValue(feedback.getEmail()));
				sb.append(StringPool.COMMA);
				sb.append(getCSVFormattedValue(feedback.getMobile()));
				sb.append(StringPool.COMMA);
				sb.append(getCSVFormattedValue(groupId));
				sb.append(StringPool.COMMA);

				sb.setIndex(sb.index() - 1);
				sb.append(CharPool.NEW_LINE);
			}
			log.info("---> sb : " + sb.toString());
			byte[] bytes = sb.toString().getBytes();
			PortletResponseUtil.sendFile(resourceRequest, resourceResponse, "FeedBackReport.csv", bytes,
					ContentTypes.APPLICATION_TEXT);
		} catch (Exception ex) {
			log.error("Exception in serveResource : ", ex);
		}
		log.info("End of serveResource");
		super.serveResource(resourceRequest, resourceResponse);
	}

	protected String getCSVFormattedValue(String value) {
		StringBundler sb = new StringBundler(3);
		sb.append(CharPool.QUOTE);
		sb.append(StringUtil.replace(value, CharPool.QUOTE, StringPool.DOUBLE_QUOTE));
		sb.append(CharPool.QUOTE);
		return sb.toString();
	}
	
	
	public String FindemployeeName(long employeeId) {
		String employeeName="";
		
		DynamicQuery empName = SgExecutiveMasterLocalServiceUtil.dynamicQuery();
		empName.add(RestrictionsFactoryUtil.eq("empId", employeeId));
		
		List<SgExecutiveMaster> executiveMasters = SgExecutiveMasterLocalServiceUtil.dynamicQuery(empName);
		
		log.info("LIST IS>>"+executiveMasters);
		
		for(SgExecutiveMaster executiveMaster1 : executiveMasters ) {
			
			employeeName=executiveMaster1.getEmployeeName();
		}
		
		log.info("NAMEOFEMP>>"+employeeName);
		return employeeName;
		
	}
	
	public String FindbranchName(long branchId) {
		String branchName="";
		
		DynamicQuery brName = SgBranchMasterLocalServiceUtil.dynamicQuery();
		brName.add(RestrictionsFactoryUtil.eq("branchId", branchId));
		
		List<SgBranchMaster> branchMasters = SgExecutiveMasterLocalServiceUtil.dynamicQuery(brName);
		
		for(SgBranchMaster branchMasters1 : branchMasters ) {
			
			branchName=branchMasters1.getBranchName();
		}
		return branchName;
		
	}
	
	
		
	public static final String[] COLUMN_NAMES = { 
		"Create Date",
		"BRANCH NAME",
		"EMPLOYEE NAME",
		"RATEOURSERV", 
		"EXECUTIVERATING",
		"CUSTCOMMENT",
		"CUSTNAME",
		"RESPONSETYPE",
		"EMAIL",
		"MOBILE",
		"GROUPID"
		
	};
	
	
	
}
