package org.zack.services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.zack.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class PDFService {

    public static void main(String[] args) throws FileNotFoundException, DocumentException {

}

public void exportPDF(String path){
    try {

        List<File> files = FileUtil.loadFileList(path);



        Document doc = init(path+"");


        // 4.添加一个内容段落
        doc.add(new Paragraph("Hello World!"));

        // 5.关闭文档
        doc.close();
    } catch (Exception e) {
        throw new RuntimeException(e);
    }

}

private Document init(String path) throws FileNotFoundException, DocumentException {
    Document document = new Document();
    PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
    document.open();
    return document;
}

}
