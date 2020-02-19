package org.afgl.biblioapp.utilities;

import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by arturo on 19/06/2017.
 * Funciones para procesar archivos XML
 */

public class XMLUtil {

    private static final String LOG_TAG = "XMLUtil";
    /*
     * Parse un archivo XML dentro de un archivo zip
     * @fileName nombre del archivo XML en el archivo zip
     * @root parser para leer el archivo XML
     */
    public static void parseXmlResource(InputStream in, ContentHandler handler){
        if(in != null){
            try{
                SAXParserFactory parserFactory = SAXParserFactory.newInstance();
                XMLReader reader = parserFactory.newSAXParser().getXMLReader();
                reader.setContentHandler(handler);

                try{
                    InputSource source = new InputSource(in);
                    source.setEncoding("UTF-8");
                    reader.parse(source);
                } finally {
                    in.close();
                }
            }catch (ParserConfigurationException e){
                LogUtils.e(LOG_TAG, "ParserConfiguration: " + e.getMessage());
            }catch (IOException e){
                LogUtils.e(LOG_TAG, "IO: " + e.getMessage());
            }catch (SAXException e){
                LogUtils.e(LOG_TAG, "SAX: " + e.getMessage());
            }
        }
    }
}
