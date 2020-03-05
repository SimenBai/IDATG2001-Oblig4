package no.simenbai.idatg2001.obligs.two;

import java.time.LocalDate;

/**
 * The type Gold member.
 */
public class GoldMember extends BonusMember {
    /**
     * Instantiates a new Gold member.
     *
     * @param memberNo     the member no
     * @param personals    the personals
     * @param enrolledDate the enrolled date
     * @param points       the points
     */
    public GoldMember(int memberNo, Personals personals, LocalDate enrolledDate, int points) {
        super(memberNo, personals, enrolledDate, points);
    }

    @Override
    public void registerPoints(int points) {
        super.registerPoints((int) (points * FACTOR_GOLD));
    }
}

