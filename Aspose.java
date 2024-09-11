import com.aspose.cells.Workbook;
import com.aspose.cells.SaveOptions;
import com.aspose.cells.SaveFormat;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import java.io.IOException;
import java.io.OutputStream;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

public class ExcelExportPortlet extends MVCPortlet {

    @Override
    public void serveResource(ResourceRequest resourceRequest, ResourceResponse resourceResponse)
            throws IOException, PortletException {

        // Set response content type and headers for file download
        resourceResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        resourceResponse.setProperty("Content-Disposition", "attachment; filename=\"protected_file.xlsx\"");

        try (OutputStream outputStream = resourceResponse.getPortletOutputStream()) {
            // Create a new workbook
            Workbook workbook = new Workbook();
            // Access the first worksheet and add data
            workbook.getWorksheets().get(0).getCells().get("A1").putValue("Hello World");

            // Set password protection
            SaveOptions saveOptions = new SaveOptions(SaveFormat.XLSX);
            saveOptions.setPassword("yourpassword"); // Set your desired password

            // Save the workbook to the output stream with the specified options
            workbook.save(outputStream, saveOptions);

            outputStream.flush(); // Ensure all data is written out
        } catch (Exception e) {
            throw new PortletException("Error generating Excel file", e);
        }
    }
}
dependencies {
    implementation 'com.aspose:aspose-cells:23.9' // Check for the latest version on Aspose's import com.aspose.cells.Workbook;
import com.aspose.cells.WorkbookSettings;
import com.aspose.cells.Worksheet;
import com.aspose.cells.SaveOptions;
import com.aspose.cells.SaveFormat;
import java.io.File;

public class ExcelFileGenerator {

    public static void main(String[] args) {
        try {
            // Create a new workbook
            Workbook workbook = new Workbook();

            // Access the first worksheet
            Worksheet sheet = workbook.getWorksheets().get(0);
            sheet.getCells().get("A1").putValue("Hello World");

            // Save the workbook with password protection
            SaveOptions saveOptions = new SaveOptions(SaveFormat.XLSX);
            saveOptions.setPassword("yourpassword");  // Set your desired password

            // Specify the path to save the Excel file
            String filePath = "protected_file.xlsx";
            workbook.save(filePath, saveOptions);

            System.out.println("Excel file created and protected successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
