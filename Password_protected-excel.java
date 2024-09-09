import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import javax.servlet.http.HttpServletResponse;
import com.liferay.portal.kernel.util.PortalUtil;

compileOnly group: 'org.apache.poi', name: 'poi', version: '5.2.3'
compileOnly group: 'org.apache.poi', name: 'poi-scratchpad', version: '5.2.3'
compileOnly group: 'org.apache.poi', name: 'poi-ooxml', version: '5.2.3'
compileOnly group: 'org.apache.xmlbeans', name: 'xmlbeans', version: '5.1.1'

List<Employee> employeeList = this.employeeLocalService.getEmployees(-1, -1);
	    try {
	    	_log.info("Generate Excel --- " + employeeList);
		      HSSFWorkbook workbook = new HSSFWorkbook();
	          Sheet sheet = workbook.createSheet("Sheet1");
	          Row headerRow = sheet.createRow(0);
	          String[] headers = {"ID", "Name", "Experience"};
	            for (int i = 0; i < headers.length; i++) {
	                Cell cell = headerRow.createCell(i);
	                cell.setCellValue(headers[i]);
	                // Style header cells
	                CellStyle headerStyle = workbook.createCellStyle();
	                Font font = workbook.createFont();
	                font.setBold(true);
	                headerStyle.setFont(font);
	                cell.setCellStyle(headerStyle);
	            }
	          // Create a row and put some cells in it
	            int rowNum = 1;
	            for (Employee employee : employeeList) {
	            	Row row = sheet.createRow(rowNum++);
	                row.createCell(0).setCellValue(employee.getEmployeeId());
	                row.createCell(1).setCellValue(employee.getEmployeeName());
	                row.createCell(2).setCellValue(employee.getEmployeeExperience());
	            }

	            // Autosize columns
	            for (int i = 0; i < headers.length; i++) {
	                sheet.autoSizeColumn(i);
	            }

	          // Protect the workbook with a password
	          workbook.writeProtectWorkbook("password", "Protected by Liferay");

	          // Write the output to a ByteArrayOutputStream
	          ByteArrayOutputStream baos = new ByteArrayOutputStream();
	          workbook.write(baos);
	          workbook.close();

	          // Set the response content type and header
	          httpServletResponse.setContentType("application/vnd.ms-excel");
	          httpServletResponse.setHeader("Content-Disposition", "attachment; filename=\"protected-file.xls\"");
	          httpServletResponse.setContentLength(baos.size());

	          // Write the ByteArrayOutputStream to the response output stream
	          OutputStream out = httpServletResponse.getOutputStream();
	          baos.writeTo(out);
	          out.flush();
	          out.close();
	    } catch (Exception e) {
	      this._log.error(e.getCause(), e);
	    } 
