XSSFWorkbook workbook= new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Sheet1");
			XSSFRow headerRow = sheet.createRow(0);
			String[] headers = { "Create Date", "Name", "Email ID",	"Phone No.", "Country", "City", "Type of Feedback",	"Title", "Description", "Is Customer", "Type of Customer",	"Response Type"};

			for (int i = 0; i < headers.length; i++) {
				XSSFCell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
				// Style header cells
				XSSFCellStyle headerStyle = workbook.createCellStyle();
				XSSFFont font = workbook.createFont();
				headerStyle.setFont(font);
				cell.setCellStyle(headerStyle);
			}
			int rowNum = 1;
			for (FeedbackForm feedback : feedbackform) {
				log.info(feedback.getEmailId());
				XSSFRow row = sheet.createRow(rowNum++);
				row.createCell(0).setCellValue(dateFormat.format(feedback.getCreateDate()));
				row.createCell(1).setCellValue(feedback.getName());
				row.createCell(2).setCellValue(feedback.getEmailId());
				row.createCell(3).setCellValue(feedback.getPhoneNo());
				row.createCell(4).setCellValue(feedback.getCountry());
				row.createCell(5).setCellValue(feedback.getCity());
				row.createCell(6).setCellValue(feedback.getTypeOfFeedback());
				row.createCell(7).setCellValue(feedback.getTitle());
				row.createCell(8).setCellValue(feedback.getDescription());
				row.createCell(9).setCellValue(feedback.getIsCustomer());
				row.createCell(10).setCellValue(feedback.getTypeOFCustomer());
				row.createCell(11).setCellValue(feedback.getResponseType());
			}
			// Autosize columns
			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}
			// Write the output to a ByteArrayOutputStream
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			workbook.write(baos);
			// Set the response content type and header
			httpServletResponse.setContentType("application/vnd.ms-excel");
			httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"protected-file.xls\"");
			httpServletResponse.setContentLength(baos.size());
	
			FileOutputStream fileOut = new FileOutputStream("protected-file.xls");
			POIFSFileSystem fs = new POIFSFileSystem();
			EncryptionInfo info = new EncryptionInfo(EncryptionMode.agile);
			Encryptor encryptor = info.getEncryptor();
			encryptor.confirmPassword("password");

			// Write the workbook to the output stream
			workbook.write(encryptor.getDataStream(fs));
			fs.writeFilesystem(fileOut);
			fileOut.flush();
			fileOut.close();
			workbook.close();
			OutputStream out = httpServletResponse.getOutputStream();
			baos.writeTo(out);	          
			out.flush();
			out.close();
			
			
			
			
			
==================================================




dependencies {
	compileOnly group: "com.liferay.portal", name: "release.dxp.api"
	compileOnly group: "jstl", name: "jstl"
	compileOnly group: "org.osgi", name: "osgi.cmpn"
	compileOnly project(":modules:sbi-core-service:sbi-core-service-api")
	compileOnly project(":modules:sbi-common-api")	
	compile group: 'javax.mail', name: 'mail', version: '1.4'	

    
    compileInclude group: "org.apache.poi", name: "poi"
    compileInclude group: "org.apache.poi", name: "poi-ooxml"
    compileInclude group: "org.apache.poi", name: "poi-ooxml-schemas"
    compileInclude group: "org.apache.xmlbeans", name: "xmlbeans"
    compileInclude group: "org.apache.commons", name: "commons-collections4"    
    compileInclude group: "org.apache.commons", name: "commons-lang3"
    compileInclude group: "org.apache.commons", name: "commons-compress"    
	compileInclude group: 'org.apache.commons', name: 'commons-math3'
    
}










=======================================




Bundle-Name: sbi-feedback-form
Bundle-SymbolicName: sbi.feedback.form
Bundle-Version: 1.0.0
    
 Import-Package: \
    !com.sun.*,\
    !junit*,\
    !org.apache.avalon.framework.logger,\
    !org.apache.crimson.jaxp,\
    !org.apache.jcp.xml.dsig.internal.dom,\
    !org.apache.log,\
    !org.apache.xml.resolver*,\
    !org.bouncycastle.*,\
    !org.gjt.xpp,\
    !org.junit*,\
    !org.relaxng.datatype,\
    !org.xmlpull.v1,\
    !com.microsoft.schemas.office.*,\
    !com.zaxxer.sparsebits,\
    !org.apache.batik.*,\
    !com.graphbuilder*,\
    !org.etsi.uri.x01903.v14,\
    !org.openxmlformats.schemas.officeDocument.x2006.*,\
    !org.openxmlformats.schemas.schemaLibrary.x2006.main,\
    !net.sf.saxon.*,\
    !org.apache.commons.codec.binary,\
    !org.apache.commons.codec.digest,\
    !com.github.luben.*,\
    !org.brotli.*,\
    !org.tukaani.xz,\
    *
	
	
===============================================================





<%@ include file="init.jsp"%>

<portlet:resourceURL var="generateReportURL" id="feedback">
	<portlet:param name="fromDate" value="FROM_DATE" />
	<portlet:param name="toDate" value="TO_DATE" />
</portlet:resourceURL>

<div class="form-group">
	<h5 class="inner-page-main-title text-left">
		<liferay-ui:message key="sbi-feedback-report.caption" />
	</h5>
</div>

<div class="form-group">
	<label class="col-md-6 col-sm-4 col-xs-12 form-label text-right"><liferay-ui:message
			key="feedback-report.fromdate" /></label>
	<div class="col-md-6 col-sm-8 col-xs-12">
		<div class="input-group datepicker date">
			<input class="form-control" data-date-format="dd/mm/yyyy"
				placeholder="DD-MM-YYYY" type="text"
				name="<portlet:namespace/>fromDate" id=fromDate required> <span
				class="input-group-addon"> <span
				class="glyphicon glyphicon-calendar">&nbsp;</span>
			</span>
		</div>
	</div>
</div>
<div class="form-group">
	<label class="col-md-6 col-sm-4 col-xs-12 form-label text-right"><liferay-ui:message
			key="feedback-report.todate" /></label>
	<div class="col-md-6 col-sm-8 col-xs-12">
		<div class="input-group datepicker date">
			<input class="form-control" data-date-format="dd/mm/yyyy"
				placeholder="DD-MM-YYYY" type="text"
				name="<portlet:namespace/>toDate" id=toDate required> <span
				class="input-group-addon"> <span
				class="glyphicon glyphicon-calendar">&nbsp;</span>
			</span>
		</div>
	</div>
</div>

<div class="from group export-csv-btn text-center">
	<button class="btn btn-pc-2 btn-md " id="generateReport"
		name="generateReport" value="Generate Report">
		<liferay-ui:message key="feedback-report.generate.report" />
	</button>
</div>
<script type="text/javascript">
	$(document).ready(
			function() {

				$('#generateReport').on(
						"click",
						function() {
							var fromDate = $('#fromDate').val();
							var toDate = $('#toDate').val();
							var url = '${generateReportURL}';
							url = url.replace("FROM_DATE", fromDate).replace(
									"TO_DATE", toDate);
							if(fromDate==''|| toDate==''){
								
								alert("Please Select Date");
							}
							else {
								try {
									if (validateURL(url)) {
										window.location.href = url;
									} else {
										throw new InvalidURLException();
									}
								} catch (e) {
									if (e instanceof InvalidURLException)
										alert(e.message);
								}
							}
							//window.location.href = url;
						});
				function validateURL(url) {
					var url = parseURL(url);
					var urlHostname = url.hostname.trim();

					if (urlHostname == '') {
						return true;
					} else {
						if (urlHostname.toUpperCase() == location.hostname
								.trim().toUpperCase()) {
							return true;
						} else
							return false;
					}
				}

				function parseURL(url) { 
					var a = document.createElement('a');
					a.href = url;
					return {
						source : url,
						protocol : a.protocol.replace(':', ''),
						hostname : a.hostname,
						host : a.host,
						port : a.port,
						query : a.search,
						params : (function() {
							var ret = {}, seg = a.search.replace(/^\?/,
									'').split('&'), len = seg.length, i = 0, s;
							for (; i < len; i++) {
								if (!seg[i]) {
									continue;
								}
								s = seg[i].split('=');
								ret[s[0]] = s[1];
							}
							return ret;
						})(),
						file : (a.pathname.match(/\/([^\/?#]+)$/i) || [
								, '' ])[1],
						hash : a.hash.replace('#', ''),
						path : a.pathname.replace(/^([^\/])/, '/$1'),
						relative : (a.href.match(/tps?:\/\/[^\/]+(.+)/) || [
								, '' ])[1],
						segments : a.pathname.replace(/^\//, '').split(
								'/')
					};
				}	

			});
</script>
