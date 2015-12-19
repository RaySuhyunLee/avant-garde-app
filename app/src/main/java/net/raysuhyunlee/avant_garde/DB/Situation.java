package net.raysuhyunlee.avant_garde.DB;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * Created by SuhyunLee on 2015. 12. 19..
 */
@Table(name = "Situations")
public class Situation extends Model {
    @Column(name = "Name", index = true)
    public String name;

    public List<FingerMap> getFingerMaps() {
        return new Select()
                .from(FingerMap.class)
                .where("SituationId = ?", this.getId())
                .execute();
    }
}
