package edu.najah.cap.Converter;

public class ConvertFactory {
    public static IConvert getConverter(String type){
        if(type.equals("PdfToZip")){
            return new ConvertPDFtoZip();
        }
        if(type.equals("ZipToPdf")){
            return new ConvertZipToPdf();
        }
        if (type.equals("TextToPdf")){
            return new ConvertTextToPdf();
        }
        return null;
    }
}
