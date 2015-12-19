package net.raysuhyunlee.avant_garde.DB;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by SuhyunLee on 2015. 12. 19..
 */
@Table(name = "FingerMaps")
public class FingerMap extends Model {
    @Column(name = "Fingers", index = true)
    public String fingers;

    @Column(name = "Sentence")
    public String sentence;

    @Column(name = "SituationId", index = true)
    public long situationId;

    public FingerMap() {
        super();
    }

    public FingerMap(String fingers, String sentence, Long situationId) {
        super();
        this.fingers = fingers;
        this.sentence = sentence;
        this.situationId = situationId;
    }

    public FingerMap(boolean[] booleans, String sentence, Long situationId) {
        super();
        fromBoolean(booleans);
        this.sentence = sentence;
        this.situationId = situationId;
    }

    public Boolean[] toBoolean() {
        Boolean[] booleans = new Boolean[fingers.length()];
        for(int i=0; i < booleans.length; i++) {
            booleans[i] = (fingers.charAt(i) == '1');
        }
        return booleans;
    }

    public void fromBoolean(boolean[] booleans) {
        fingers = "";
        for(boolean b : booleans) {
            if (b) {
                fingers += "1";
            } else {
                fingers += "0";
            }
        }
    }
}
