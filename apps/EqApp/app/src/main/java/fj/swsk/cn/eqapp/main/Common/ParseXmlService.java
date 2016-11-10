package fj.swsk.cn.eqapp.main.Common;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by apple on 16/2/26.
 */
public class ParseXmlService {
    public static HashMap<String,String> parseXml(InputStream io) {
        HashMap<String,String> map = new HashMap<>();
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(io);
            Element root = doc.getDocumentElement();
            NodeList nodes = root.getChildNodes();
            for(int j = 0;j<nodes.getLength();j++){
                Node node = nodes.item(j);
                if(node.getNodeType() == Node.ELEMENT_NODE){
                    Element child = (Element)node;
                    map.put(child.getNodeName(),child.getTextContent()
//                            child.getNodeValue()
 );
                }
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return map;
    }
}
