package no.simenbai.idatg2001.obligs.four;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


/**
 * The type Bonus member.
 */
public abstract class BonusMember {

  /**
   * The multiplication factor for silver bonus.
   */
  static final float FACTOR_SILVER = 1.2F;
  /**
   * The multiplication factor for gold bonus.
   */
  static final float FACTOR_GOLD = 1.5F;

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
    if (personals == null) {
      throw new IllegalArgumentException("Personal information is null");
    } else if (enrolledDate == null) {
      throw new IllegalArgumentException("Enrolled date is null");
    }
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
    if (personals == null) {
      throw new IllegalArgumentException("Personal information is null");
    } else if (enrolledDate == null) {
      throw new IllegalArgumentException("Enrolled date is null");
    }
    this.memberNo = memberNo;
    this.personals = personals;
    this.enrolledDate = enrolledDate;
    this.bonusPoints = 0;
  }

  /**
   * Find amount qualification points.
   *
   * @param date the date
   * @return the int
   */
  public int findQualificationPoints(LocalDate date) {
    if (date == null) {
      throw new IllegalArgumentException("Date can't be null");
    }
    if (ChronoUnit.DAYS.between(this.enrolledDate, date) < 365) {
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
  public void registerPoints(int points) {
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


  /**
   * Get's the type of the member.
   *
   * @param member The member you want to check
   * @return The type of the member
   */
  public static MemberType getMemberType(BonusMember member) {
    if (member instanceof BasicMember) {
      return MemberType.BASICMEMBER;
    } else if (member instanceof SilverMember) {
      return MemberType.SILVERMEMBER;
    } else {
      return MemberType.GOLDMEMBER;
    }
  }

  /**
   * A member type enum to make it easier to handle the different types.
   */
  enum MemberType {
    BASICMEMBER("Basic member"),
    SILVERMEMBER("Silver member"),
    GOLDMEMBER("Gold member");

    private String name;

    MemberType(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return this.name;
    }
  }
}