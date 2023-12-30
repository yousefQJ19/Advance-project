package edu.najah.cap.Converter;

import edu.najah.cap.Converter.IConvert;

import java.io.IOException;

public class ConvertContext {
    private IConvert convertContext;

    public void setContext(IConvert context){
        this.convertContext=context;
    }

    public void getContext(String inputPath,String outputPath) throws IOException {
        convertContext.Convert(inputPath,outputPath);
    }

}
