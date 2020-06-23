/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hoangng.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author popem
 */
public class Tester {

    public static void main(String[] args) {
        XMLWellformChecker checker = new XMLWellformChecker();

        Map<String, String> map = new LinkedHashMap<>();
        map.put("Attribute không value", "<h1 checked>YEAH,/h1>");
        map.put("Value không bọc trong cặp nháy ", "<h1 aa= aa><img a=a />YEAH</h1>");
        map.put("Attribute dính liền nhau", "<h1 a=\"1\"b='2'c=3>YEAH</h1>");
        map.put("Empty element", "<h1><img src=\"\"><br><hr/></h1>");
        map.put("Lỗi đóng mở thẻ", "<li><a>Sach Moi</a><h3>");

        for (Map.Entry<String, String> entry : map.entrySet()) {
            System.out.println(entry.getKey());

            System.out.println(entry.getValue());

            System.out.println(checker.check(entry.getValue()));

            System.out.println();
        }

    }
}
