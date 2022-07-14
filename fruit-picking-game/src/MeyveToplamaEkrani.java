
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFrame;


public class MeyveToplamaEkrani extends JFrame{

    public MeyveToplamaEkrani(MeyveToplamaMain oyun) throws HeadlessException {
        this.setTitle("Meyve Toplama Oyunu");
        this.setResizable(false);
        this.setFocusable(false);
        this.setSize(1280, 768); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(oyun);
        this.setVisible(true);
    }
    
    public static void main(String[] args) {
        
        JFrame baslat = new JFrame("Başlat");
        JFrame durdur = new JFrame("Durdur");
        JButton baslatButton = new JButton();
        JButton durdurButton = new JButton();
        baslat.add(baslatButton);
        durdur.add(durdurButton);
        baslatButton.setBounds(40,50,100,30);
        baslatButton.setText("Başlat");
        durdurButton.setBounds(40,50,100,30);
        durdurButton.setText("Durdur");
        baslat.setLayout(null);
        baslat.setVisible(true);
        baslat.setSize(200,200);
        durdur.setLayout(null);
        durdur.setVisible(true);
        durdur.setSize(200,200);
        
        
        baslatButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                baslat.setVisible(false);
                
                MeyveToplamaMain oyun = new MeyveToplamaMain();
        
                oyun.setRequestFocusEnabled(true);
        
                oyun.addKeyListener(oyun);
        
                oyun.setFocusable(true);
        
                MeyveToplamaEkrani ekran = new MeyveToplamaEkrani(oyun);
                
            }
            
        });
        
        
        durdurButton.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                
                try {
                    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                } catch (InterruptedException ex) {
                    
                }
            }
            
        });
    }  
}
