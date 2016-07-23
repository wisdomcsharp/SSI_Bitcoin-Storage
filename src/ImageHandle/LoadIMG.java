package ImageHandle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;

/**
 * Created by Wisdom on 21/07/2016.
 */
public class LoadIMG {
    private BufferedImage image;
    private File file;

    public enum Type {
        PNG,
        BMP,
        JPG
    }

    public LoadIMG(File file) {
        this.file = file;
        try {
            this.image = ImageIO.read(file);
        } catch (Exception exc) {

        }
    }

    public void setBuffer(BufferedImage img) {
        this.image = img;
    }
    public BufferedImage getBuffer(){
        return this.image;
    }
    public boolean saveBuffer(File file, Type type) {
        boolean saved = false;
        try {
            String name = file.getName(), directory = file.toPath().getParent().toString(),
                    newFile = directory + "\\" + name.substring(0,name.lastIndexOf('.'))
                            + "." + type.toString().toLowerCase();

            ImageIO.write(this.image, type.toString(), new File(newFile));
            saved = true;
        } catch (Exception exc) {

        }
        return saved;
    }
    public boolean saveBuffer(Type type) {
        boolean saved = false;
        try {
            String name = this.file.getName(), directory = this.file.toPath().getParent().toString(),
                    newFile = directory + "\\" + name.substring(0,name.lastIndexOf('.'))
                            + "." + type.toString().toLowerCase();

            ImageIO.write(this.image, type.toString(), new File(newFile));
            saved = true;
        } catch (Exception exc) {

        }
        return saved;
    }
    public boolean deleteBuffer() {
        boolean deleted = false;
        try {
            if (file.exists()) {
                file.delete();
            }
            deleted = true;
        } catch (Exception exc) {

        }
        return deleted;
    }


}
