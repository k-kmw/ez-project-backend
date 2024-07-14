package com.youngcha.ez.util;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class ImageConverter {

    @Value("${com.pdf.path}")
    String pdfPath;

    @Value("${com.jpg.path}")
    String jpgPath;

    public void convertJpg(String pdfName){
        
        //기존 파일들 삭제
        File[] files = new File(jpgPath).listFiles();
        for (File f : files){
            f.delete();
        }

        File file = new File(pdfPath+"/"+pdfName+".pdf");

        try {
            PDDocument document = Loader.loadPDF(file);
            int pageCount = document.getNumberOfPages();//pdf의 페이지 수
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for(int i=0;i<pageCount;i++)
            {
                BufferedImage imageObj = pdfRenderer.renderImageWithDPI(i, 900, ImageType.RGB);//pdf파일의 페이지를돌면서 이미지 파일 변환
                File outputfile = new File(jpgPath + "/" + i + ".jpg");//파일이름 변경(.pdf->.jpg)
                ImageIO.write(imageObj, "jpg", outputfile);//변환한 파일 업로드
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        getTargetPrice(pdfName);
        getTitle(pdfName);
        getDivideFirstImg(pdfName);
    }


    public void getTargetPrice(String pdfName){

        File file = new File(pdfPath+"/"+pdfName+".pdf");

        try {
            PDFRenderer pdfRenderer = new PDFRenderer(Loader.loadPDF(file));
            BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
            imageObj = imageObj.getSubimage(1650, 600, 700, 220);
            File outputfile = new File(jpgPath + "/" + "targetPrice" + ".jpg");//파일이름 변경(.pdf->.jpg)
                ImageIO.write(imageObj, "jpg", outputfile);//변환한 파일 업로드

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getTitle(String pdfName){

        File file = new File(pdfPath+"/"+pdfName+".pdf");

        try {
            PDFRenderer pdfRenderer = new PDFRenderer(Loader.loadPDF(file));
            BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 300, ImageType.RGB);
            imageObj = imageObj.getSubimage(150, 550, 1500, 150);
            File outputfile = new File(jpgPath + "/" + "title" + ".jpg");//파일이름 변경(.pdf->.jpg)
            ImageIO.write(imageObj, "jpg", outputfile);//변환한 파일 업로드

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getDivideFirstImg(String pdfName){

        File file = new File(pdfPath+"/"+pdfName+".pdf");

        try {
            PDFRenderer pdfRenderer = new PDFRenderer(Loader.loadPDF(file));
            BufferedImage imageObj = pdfRenderer.renderImageWithDPI(0, 700, ImageType.RGB);
            imageObj = imageObj.getSubimage(360, 1200, 3500, 6800);

            //기존 파일들 삭제
            File[] files = new File(jpgPath).listFiles();
            for (File f : files){
                if(f.getName().equals("0.jpg")){
                    f.delete();
                }
            }


            File outputfile = new File(jpgPath + "/" + "0" + ".jpg");//파일이름 변경(.pdf->.jpg)
            ImageIO.write(imageObj, "jpg", outputfile);//변환한 파일 업로드

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
