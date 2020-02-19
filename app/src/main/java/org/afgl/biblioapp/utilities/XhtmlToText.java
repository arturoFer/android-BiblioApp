package org.afgl.biblioapp.utilities;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

/**
 * Created by arturo on 19/06/2017.
 * Extrae el texto de un archivo xhtml
 */

public class XhtmlToText extends DefaultHandler {
    /*
     * Nodos que necesitam que su contenido sea seguido por un espacio en blanco
     */
    private static final String[] ADD_WHITE_SPACE_NODES = { "br", "p", "h1", "h2", "h3", "h4", "h5", "h6"};

    /*
     * recorta el texto en strings de aproximadamente doscientas palabras
     */
    private final static int MIN_CHARS_PER_STRING = 10*200;

    private StringBuilder builder;
    private ArrayList<String> text;
    private boolean inBody = false;


    public XhtmlToText(ArrayList<String> text){
        this.text = text;
        builder = new StringBuilder();
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        //ignora el texto en head
        if(inBody){
            builder.append(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        if(isWhiteSpaceNode(localName) || MIN_CHARS_PER_STRING < builder.length()){
            flushAccumulator();
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
        // metemos el texto restante en text
        flushAccumulator();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        if(localName.equalsIgnoreCase("li")){
            builder.append(" ");
        } else if(localName.equalsIgnoreCase("body")){
            inBody = true;
        }
    }

    private void flushAccumulator(){
        String aux = builder.toString();
        aux = aux.replaceAll("\\s+", " ");
        aux = aux.trim();
        //text.add(builder.toString());
        if(!aux.equals("")) {
            text.add(aux);
        }
        builder.setLength(0);
    }

    private boolean isWhiteSpaceNode(String nodeName){
        for(String s : ADD_WHITE_SPACE_NODES){
            if(s.equals(nodeName)){
                return true;
            }
        }
        return false;
    }
}
