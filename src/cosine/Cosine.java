/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cosine;

import com.sun.org.apache.xml.internal.utils.StylesheetPIHandler;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Cosine {

    static Connection kon;
    
    static Statement st;
    public static void main(String[] args) throws IOException, SQLException {
        String dok = add_doc();
        String[] kalimat = dok.split("\\.");
        Vector<String> index = new <String>Vector();
        Vector<String> dokumen = new <String>Vector();
        for (int i = 0; i < kalimat.length; i++) {
            dokumen.add(kalimat[i]);
        }
        
        for (int i = 0; i < dokumen.size(); i++) {
            String[] kata = dokumen.get(i).split(" ");
            for (int j = 0; j < kata.length; j++) {
                if (kata[j].isEmpty()||kata[j].contains(" ")||kata[j].matches("\\S")) {
                }else{
                   //proses add index
                    int hitung = 0;
                    for (int k = 0; k < index.size(); k++) {
                        if (index.size() == 0) {
                            index.add(kata[j]);
                        }else{
                            if(!kata[j].equalsIgnoreCase(index.get(k))){
                                hitung++;
                            }
                        }
                    }
                    if (hitung == index.size()) {
                        index.add(kata[j]);
                    }
                    
                }
                
            }
        }
        
        int[][] hitung_kata = new int[index.size()][dokumen.size()];
        
        for (int i = 0; i < dokumen.size(); i++) {
            String[] kata = dokumen.get(i).split(" ");
            for (int j = 0; j < kata.length; j++) {
                for (int k = 0; k < index.size(); k++) {
                    if (kata[j].equalsIgnoreCase(index.get(k))) {
                        hitung_kata[k][i] = hitung_kata[k][i]+1;
                    }
                }
            }
        }
        Vector<Integer> penyebut_pertama = new <Integer>Vector();
        int tampung = 0;
        for (int i = 0; i < dokumen.size(); i++) {
            for (int j = 0; j < index.size(); j++) {
                tampung = tampung + hitung_kata[j][i];
            }
            penyebut_pertama.add(tampung);
        }
        
        
        BigDecimal penyebut = new BigDecimal("1");
        BigInteger final_penyebut ;
        for (int i = 0; i < penyebut_pertama.size(); i++) {
            int ht = 0;
            ht = penyebut_pertama.get(i);
            penyebut = penyebut.multiply(BigDecimal.valueOf(ht));
        }
        System.out.println(penyebut);
        
        Vector<Double> pembilang_pertama = new <Double>Vector();
        
        for (int i = 0; i < dokumen.size(); i++) {
            double hitung = 0;
            for (int j = 0; j < dokumen.size(); j++) {
                for (int k = 0; k < index.size(); k++) {
                    
                    hitung = hitung + (hitung_kata[k][j]*hitung_kata[k][j]);
                    hitung = Math.sqrt(hitung);
                }
                
                pembilang_pertama.add(hitung);
            }
        }
        
        BigDecimal pembilang = new BigDecimal("0");
        for (int i = 0; i < pembilang_pertama.size(); i++) {
            double ht = 0;
            ht = pembilang_pertama.get(i);
            pembilang = pembilang.add(BigDecimal.valueOf(ht));
        }
        System.out.println("/");
        System.out.println(pembilang);
        System.out.println("=");
        BigDecimal hasil;
        hasil = penyebut.divide(pembilang);
        System.out.println(hasil);
        
        
        
//        for (int i = 0; i < hitung_kata.length; i++) {
//            for (int j = 0; j < hitung_kata[i].length; j++) {
//                System.out.print(hitung_kata[i][j]);
//            }
//            System.out.println("");
//        }
//        
//        
//        System.out.println(dokumen.size());
//        
    }
    
    private static void konek() throws SQLException {
        String dbHost = "jdbc:mysql://localhost:3306/uninform";
        String dbUser = "root";
        String dbPass = "";

        try {
            kon = (Connection) DriverManager.getConnection(dbHost, dbUser, dbPass);
            //System.out.println("SUKSES !!!");

        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    
    private static String add_doc() throws IOException, SQLException{
        String url = "http://www.uinsby.ac.id";
        Document doc = Jsoup.connect(url).timeout(0).get();
        Elements text = doc.select("body");
        String teks = text.text();
        teks = teks.replaceAll("[(-+^:,'|&?!)]", "").replaceAll("yang", "").replaceAll("dengan", "").replaceAll("dan", "").replaceAll("dari", "");
        String dokumen = teks;
        
        return dokumen;
    }
    
}
