
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
