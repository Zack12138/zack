package org.zack.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.util.StringUtils;
import org.zack.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class PDFService {

    public static void main(String[] args) throws FileNotFoundException, DocumentException {
        PDFService pdfService = new PDFService();
        pdfService.exportPDF("D:\\VOL\\1\\迅雷下载\\和谐物\\整理\\2云长\\.本子\\调教家政妇\\img");

    }

public void exportPDF(String path){
    try {

        List<File> files = FileUtil.loadFileList(path);

        String name = StringUtils.unqualify(path,'\\');

        Document doc = init(path+"\\"+name+".pdf");

        for (File file:files) {

            Image img = Image.getInstance(file.toURI().toURL());
            float width = img.getWidth();
            float height = img.getHeight();
            setPageSize(doc,width,height);
            doc.newPage();
            img.setAbsolutePosition(0, 0);
 //           img.scaleAbsolute(595, 842);
            doc.add(img);
        }

        doc.close();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }

}

private Document init(String path) throws FileNotFoundException, DocumentException {
    Document document = new Document(PageSize.A4, 0, 0, 0, 0);
    File file = new File(path);
    if (file.exists())
        file.delete();
    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
    document.open();
    return document;
}

    private  void setPageSize(Document document,float width,float heigth){
        //竖向
        Rectangle pageSize = new Rectangle(width, heigth);
        // pageSize.rotate();
        document.setPageSize(pageSize);
    }

}
