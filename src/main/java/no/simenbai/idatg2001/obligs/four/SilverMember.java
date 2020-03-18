package no.simenbai.idatg2001.obligs.four;

import java.time.LocalDate;

/**
 * The type Silver member.
 */
public class SilverMember extends BonusMember {
    /**
     * Instantiates a new Silver member.
     *
     * @param memberNo     the member no
     * @param personals    the personals
     * @param enrolledDate the enrolled date
     * @param points       the points
     */
    public SilverMember(int memberNo, Personals personals, LocalDate enrolledDate, int points) {
        super(memberNo, personals, enrolledDate, points);
    }

    @Override
    public void registerPoints(int points) {
        super.registerPoints((int) (points * FACTOR_SILVER));
    }
}
