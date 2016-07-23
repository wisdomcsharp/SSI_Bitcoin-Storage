package ImageHandle;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import ExceptionList.NullCarrier;
import org.steganography.carrier.CommonCarrierImage;
import org.steganography.error.*;
import org.steganography.hiding.HidingText;
import org.steganography.misc.ImageManager;
import org.steganography.schemes.image.access.InvertedSequentialImageAccessScheme;
import org.steganography.schemes.image.hiding.DualLSBImageHidingScheme;
import org.steganography.schemes.modulation.AlternateInversionComponentModulationScheme;

import javax.imageio.ImageIO;

/**
 * Simple example to start with <tt>org.steganography</tt> library.<br/>
 * This source code reads a PNG image (default "Carrier.png") from a root path
 * and encodes a textual component having value "Secret message" into another
 * PNG image (default "Hidden.png"). It then reads the stego-image
 * ("Hidden.png"), extracts the secret message from it and prints it.<br/>
 * A suitabe carrier image can be found on the "Software" section of the
 * "Hide & Reveal" website
 *
 * @author ncottin
 */
//JPEG not support
public class Steganography {

    private File file;
    private ImageManager imageManager;
    private RenderedImage carrier; //carries image
    private CommonCarrierImage ci;
    private File output;

    public Steganography(File file, File output) throws NullCarrier, IOException, InvalidTypeException {
        this.file = file;
        this.output = output;

            // Step 1. Read the carrier image
            this.imageManager = new ImageManager();
            this.ci = new CommonCarrierImage(
                    new InvertedSequentialImageAccessScheme(),
                    new DualLSBImageHidingScheme(),
                    new AlternateInversionComponentModulationScheme());
            this.carrier = this.imageManager.loadImage(file);
            if (this.carrier == null) {
                throw new NullCarrier();
            }
            ci.setCarrierImage(this.carrier);
    }
    public void setText(String text) throws HidingComponentSizeException, CarrierInitializationException, CarrierSizeException, CarrierAccessSchemeException, CompatibilityException, HidingException, CarrierHidingSchemeException, HidingComponentInitializationException {
   //     System.out.print(this.ci == null);
   //     this.ci.setCarrierImage(this.carrier);


        // Step 2. Create a hiding component which contains
        // textual information
        HidingText hidingComponent = new HidingText();
        hidingComponent.setText(text);
        this.ci.hide(hidingComponent);
    }
    public String getText() throws HidingComponentSizeException, CarrierInitializationException, CarrierSizeException, CarrierAccessSchemeException, CompatibilityException, RevealingException, CarrierHidingSchemeException, HidingComponentInitializationException, IOException, InvalidTypeException {
        // Step 4. Reveal the hidden text from the stego-image
        // and print it
        carrier = imageManager.loadImage(this.file);
        ci.setCarrierImage(carrier);

        HidingText revealedComponent = new HidingText();
        ci.reveal(revealedComponent);
        return revealedComponent.getText();
    }
    public void saveImage() throws IOException, InvalidTypeException{
        // Step 3. Save to another (non compressed) file
        ImageManager.writeImage(
                ci.getCarrierImage(),
                output, true);
    }
    public void close(){
        // Step 5. Terminate the process (this step should not
        // be omitted to make sure that memory is liberated)
        imageManager.terminate();
    }

}