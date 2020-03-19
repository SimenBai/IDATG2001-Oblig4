package no.simenbai.idatg2001.obligs.four;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The Member archive test.
 */
class MemberArchiveTest {

  private LocalDate testDate;
  private Personals ole;
  private MemberArchive memberArchive;
  private int oleMemberNo;


  private static final Logger LOGGER = Logger.getLogger(MemberArchiveTest.class.getName());

  /**
   * Sets up the tests.
   */
  @BeforeEach
  void setUp() {
    this.memberArchive = new MemberArchive();
    this.testDate = LocalDate.of(2008, 2, 10);
    this.ole = new Personals("Olsen", "Ole",
        "ole.olsen@dot.com", "ole");

    this.oleMemberNo = this.memberArchive.addMember(this.ole, testDate);
  }

  /**
   * Test if the addMember function works as intended.
   */
  @Test
  void addMember() {
    this.memberArchive.addMember(this.ole, testDate);
    BonusMember member = this.memberArchive.members.get(this.memberArchive.members.size() - 1);
    LOGGER.info("addMember() - Checking points");
    assertEquals(0, member.getPoints());
    LOGGER.info("addMember() - Checking personals");
    assertEquals(this.ole, member.getPersonals());
    LOGGER.info("addMember() - Checking enrolledDate");
    assertEquals(this.testDate, member.getEnrolledDate());
    LOGGER.info("addMember() - Checking class");
    assertEquals(member.getClass(), BasicMember.class);
  }

  /**
   * Test if the findPoints() function works as intended.
   */
  @Test
  void findPoints() {
    LOGGER.info("findPoints() - Testing correct member no, and password, no points");
    int points = this.memberArchive.findPoints(this.oleMemberNo, "ole");
    assertEquals(0, points);

    LOGGER.info("findPoints() - Testing correct member no, and wrong password");
    points = this.memberArchive.findPoints(this.oleMemberNo, "o");
    assertEquals(-1, points);

    LOGGER.info("findPoints() - Testing wrong member no, and correct password");
    points = this.memberArchive.findPoints(0, "ole");
    assertEquals(-1, points);

    this.memberArchive.registerPoints(this.oleMemberNo, 1000);
    LOGGER.info("findPoints() - Testing correct member no, and password, with points");
    points = this.memberArchive.findPoints(this.oleMemberNo, "ole");
    assertEquals(1000, points);
  }

  /**
   * Test if the registerPoints() function works as intended.
   */
  @Test
  void registerPoints() {
    LOGGER.info("registerPoints() - Adding 1000 points, with correct member");
    boolean response = this.memberArchive.registerPoints(this.oleMemberNo, 1000);
    int points = this.memberArchive.findPoints(this.oleMemberNo, "ole");
    assertEquals(1000, points);
    assertTrue(response);

    LOGGER.info("registerPoints() - Adding 1000 points, with wrong member");
    response = this.memberArchive.registerPoints(0, 1000);
    assertFalse(response);
  }

  /**
   * Check if the checkMembers() function works as intended.
   */
  @Test
  void checkMembers() {
    BonusMember member = this.memberArchive.members.get(0);

    LOGGER.info("checkMembers() - Testing a member with 0 points");
    this.memberArchive.checkMembers(testDate);
    assertTrue(member instanceof BasicMember);

    LOGGER.info("checkMembers() - Testing a member with 25000 points");
    this.memberArchive.registerPoints(this.oleMemberNo, 25000);
    this.memberArchive.checkMembers(testDate);
    member = this.memberArchive.members.get(0);
    assertTrue(member instanceof SilverMember);

    LOGGER.info("checkMembers() - Testing a member with over 75000 points");
    this.memberArchive.registerPoints(this.oleMemberNo, 50000);
    this.memberArchive.checkMembers(testDate);
    member = this.memberArchive.members.get(0);
    assertTrue(member instanceof GoldMember);
  }
}