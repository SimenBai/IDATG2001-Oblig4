package no.simenbai.idatg2001.obligs.two;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;


/**
 * The type Bonus member.
 */
public class BonusMember {
    /**
     * The multiplication factor for silver bonus.
     */
    final float FACTOR_SILVER = 1.2F;
    /**
     * The multiplication factor for gold bonus.
     */
    final float FACTOR_GOLD = 1.5F;

    private final int memberNo;
    private final Personals personals;
    private final LocalDate enrolledDate;
    private int bonusPoints = 0;

    /**
     * Instantiates a new Bonus member.
     *
     * @param memberNo     the member no
     * @param personals    the personals
     * @param enrolledDate the enrolled date
     * @param points       the points
     */
    BonusMember(int memberNo, Personals personals, LocalDate enrolledDate, int points) {
        this.memberNo = memberNo;
        this.personals = personals;
        this.enrolledDate = enrolledDate;
        this.bonusPoints = points;
    }

    /**
     * Instantiates a new Bonus member.
     *
     * @param memberNo     the member no
     * @param personals    the personals
     * @param enrolledDate the enrolled date
     */
    BonusMember(int memberNo, Personals personals, LocalDate enrolledDate) {
        this.memberNo = memberNo;
        this.personals = personals;
        this.enrolledDate = enrolledDate;
        this.bonusPoints = 0;
    }

    /**
     * Find amount qualification points
     *
     * @param date the date
     * @return the int
     */
    public int findQualificationPoints(LocalDate date) {
        if(ChronoUnit.DAYS.between(this.enrolledDate, date) < 365){
            return getPoints();
        }
        return 0;
    }

    /**
     * Checks if the password is ok.
     *
     * @param password the password
     * @return the boolean
     */
    public boolean okPassword(String password) {
        return getPersonals().okPassword(password);
    }

    /**
     * Register points.
     *
     * @param points the points
     */
    public void registerPoints(int points){
        this.bonusPoints += points;
    }

    /**
     * Gets member no.
     *
     * @return the member no
     */
    public int getMemberNo() {
        return memberNo;
    }

    /**
     * Gets personals.
     *
     * @return the personals
     */
    public Personals getPersonals() {
        return personals;
    }

    /**
     * Gets enrolled date.
     *
     * @return the enrolled date
     */
    public LocalDate getEnrolledDate() {
        return enrolledDate;
    }

    /**
     * Gets points.
     *
     * @return the points
     */
    public int getPoints() {
        return bonusPoints;
    }
}