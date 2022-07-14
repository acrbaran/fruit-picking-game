
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MeyveToplamaMain extends JPanel implements KeyListener,ActionListener{
 
    private String kullaniciAdi = "root";
    private String parola = "";
    private String db_ismi = "demo";
    private String host = "localhost";
    private int port = 3306;
    
    private Connection connect = null;
    
    private Statement statement = null;
    
    
    Timer sayac = new Timer(1, this);
    long baslangic = System.currentTimeMillis();
    double saniye;
    int skor = 0;
    
    private int toplananMeyve=0;
    private int atilanElma = 0;
    
    private BufferedImage agac;
    private BufferedImage sepet;
    private BufferedImage elma_p;
    
    private ArrayList<Elma> elmalar = new ArrayList<Elma>();
    
    private int elmaY = -1;
    
    private int sepetX = 0;
    
    private int sepetHareket = 2;
    
    private int agacX = 0;
    
    private int agacHareket = 20;
    
    public MeyveToplamaMain() {
        
        VeriTabaninaBaglan();
       
        ResimleriYukle();
        
        setBackground(Color.WHITE);
        sayac.start();
    }
    
    public void ResimleriYukle()
    {
        try {
        	agac = ImageIO.read(new FileImageInputStream(new File("agac.png")));
        	sepet = ImageIO.read(new FileImageInputStream(new File("sepet.png")));
        	elma_p = ImageIO.read(new FileImageInputStream(new File("elma.png")));
        } catch (IOException ex) {
            Logger.getLogger(MeyveToplamaMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void VeriTabaninaBaglan()
    {
        String url = "jdbc:mysql://"+host+":"+port+"/"+db_ismi;
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MeyveToplamaMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            connect = (Connection) DriverManager.getConnection(url, kullaniciAdi, parola);
            System.out.println("Bağlantı Başarılı...");
        } catch (SQLException ex) {
            System.out.println("Bağlantı Başarısız...");
        }
    }
    
    public boolean CarptiMi()
    {
        for(Elma elma:elmalar)
        {                  
            if(elma.getX()==sepetX && elma.getY()>630 &&  elma.getY()<670)
             {
            	
                elmalar.remove(elma);
                toplananMeyve++;
                skor+=1000;
                
                if(toplananMeyve==3){
                    return true;
                }               
            } 
        }
        return false;
    }
    
    public void VeriTabaninaYaz()
    {
        try {
            statement = connect.createStatement();           
            
            String sorgu = "Insert Into meyvetoplamagame (Skor,HarcananMeyve,ToplananMeyve) "
                    + "VALUES(" + "'" +skor+ "',"+"'" +atilanElma+ "',"+"'" +toplananMeyve+ "')";
            
            statement.executeUpdate(sorgu);
        } catch (SQLException ex) {
            Logger.getLogger(MeyveToplamaMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void DosyaYaz()
    {
        try{
        File file = new File("dosya.txt");
                
                if (!file.exists()) {
                    file.createNewFile();
                }
                
                FileWriter fileWriter = new FileWriter(file, false);
                BufferedWriter bWriter = new BufferedWriter(fileWriter);
                bWriter.write("Skor: "+skor+"\nGecen Sure: "+saniye+
                        "\nHarcanan Meyve: "+atilanElma+"\nToplanan Meyve: "+toplananMeyve);
                bWriter.close();
                System.exit(0);
            } catch (IOException ex) {
                Logger.getLogger(MeyveToplamaMain.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    @Override
    public void paint(Graphics g) {
    	
        super.paint(g);
        g.drawImage(sepet, sepetX, 640, agac.getWidth() / 5,agac.getHeight() / 7,this);
        g.drawImage(agac, agacX, 0, agac.getWidth() / 8,agac.getHeight() / 8,this);
        
        for(Elma elma:elmalar){
            g.drawImage(elma_p, elma.getX(), elma.getY(), agac.getWidth() / 10,agac.getHeight() / 10,this);
        }
        
        for(Elma elma:elmalar){
            if(elma.getY() <= 0){
            	elmalar.remove(elma);
            }
        }
       
        
        
        if(CarptiMi())
        {
                sayac.stop();
                long bitis = System.currentTimeMillis();
                long gecenSure = bitis - baslangic;
                saniye = (double)gecenSure/1000;
                skor = (skor / ((int) saniye * atilanElma))  * 10;
                String message = "Tebrikler Kazandiniz.\n"+
                        "Harcanan Meyve Sayisi : "+atilanElma+"\n"+
                        "Toplanan Meyve Sayisi: "+toplananMeyve+"\n"+
                        "Skor: "+skor+"\n"+
                        "Gecen Sure: "+saniye+" saniye";
                
                JOptionPane.showMessageDialog(this, message);
                    
                VeriTabaninaYaz();
                DosyaYaz();
        }
    }

    @Override
    public void repaint() {
        super.repaint();
    }
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {      
        
        if(e.getKeyCode() == KeyEvent.VK_LEFT){
            if(agacX <= 0){
            	agacX = 0;
            }
            else{
            	agacX -= agacHareket;
            }
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(agacX >= 1200){
            	agacX = 1200;
            }
            else {
            	agacX += agacHareket;
            }
        }
        else if(e.getKeyCode() == KeyEvent.VK_SPACE){
        	elmalar.add(new Elma(agacX+8,35));
            atilanElma++;
        }
        else if(e.getKeyCode() == KeyEvent.VK_CONTROL){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MeyveToplamaMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {

    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

        for(Elma elma : elmalar){
        	elma.setY(elma.getY( )- elmaY);
        }
        sepetX += sepetHareket;
        if(sepetX >= 1150){
        	sepetHareket = -sepetHareket;
        }
        else if(sepetX == 0){
        	sepetHareket = -sepetHareket;
        }
        repaint();
    }
}