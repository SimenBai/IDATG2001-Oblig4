package no.simenbai.idatg2001.obligs.four;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class used to test the calculation of bonus points in the different classes representing the
 * different memberships. To use this class, you first have to set up the Unit-test framework in
 * your IDE.
 */
class BonusMemberTest {

  private LocalDate testDate;
  private Personals ole;
  private Personals tove;

  private static final Logger LOGGER = Logger.getLogger(BonusMemberTest.class.getName());


  @BeforeEach
  void setUp() {
    this.testDate = LocalDate.of(2008, 2, 10);
    this.ole = new Personals("Olsen", "Ole",
        "ole.olsen@dot.com", "ole");
    this.tove = new Personals("Hansen", "Tove",
        "tove.hansen@dot.com", "tove");
  }

  /**
   * Tests the accuracy of the calculation of points for the basic member Ole. Ole was registered as
   * Basic Member more than 365 days before 10/2-2008, and should not qualify for upgrade.
   */
  @Test
  void testBasicMemberOle() {
    BasicMember b1 = new BasicMember(100, ole,
        LocalDate.of(2006, 2, 15));
    b1.registerPoints(30000);
    LOGGER.info("Test nr 1: No qualification points");
    assertEquals(0, b1.findQualificationPoints(testDate));
    assertEquals(30000, b1.getPoints());

    LOGGER.info("Test nr 2: Adding 15 000 points, still no qualification points");
    b1.registerPoints(15000);
    assertEquals(0, b1.findQualificationPoints(testDate));
    assertEquals(45000, b1.getPoints());
  }

  /**
   * Tests the accuracy of the calculation of points for the absic member Tove, who was registered
   * with basic membership less than 365 days before 10/2-2008, and hence does qualify for an
   * upgrade, first to Silver, then to Gold.
   */
  @Test
  void testBasicMemberTove() {
    BasicMember b2 = new BasicMember(110, tove,
        LocalDate.of(2007, 3, 5));
    b2.registerPoints(30000);

    LOGGER.info("Test nr 3: Tove should qualify");
    assertEquals(30000, b2.findQualificationPoints(testDate));
    assertEquals(30000, b2.getPoints());

    LOGGER.info("Test nr 4: Tove as silver member");
    SilverMember b3 = new SilverMember(b2.getMemberNo(), b2.getPersonals(),
        b2.getEnrolledDate(), b2.getPoints());
    b3.registerPoints(50000);
    assertEquals(90000, b3.findQualificationPoints(testDate));
    assertEquals(90000, b3.getPoints());

    LOGGER.info("Test nr 5: Tove as gold member");
    GoldMember b4 = new GoldMember(b3.getMemberNo(), b3.getPersonals(),
        b3.getEnrolledDate(), b3.getPoints());
    b4.registerPoints(30000);
    assertEquals(135000, b4.findQualificationPoints(testDate));
    assertEquals(135000, b4.getPoints());

    LOGGER.info("Test nr 6: Changed test date on Tove");
    testDate = LocalDate.of(2008, 12, 10);
    assertEquals(0, b4.findQualificationPoints(testDate));
    assertEquals(135000, b4.getPoints());

  }

  /**
   * Tests the passwords of both members.
   */
  @Test
  void testPasswords() {
    LOGGER.info("Test nr 7: Trying wrong password on Ole");
    assertFalse(ole.okPassword("000"));
    LOGGER.info("Test nr 8: Trying correct password on Tove.");
    assertTrue(tove.okPassword("tove"));
  }

  /**
   * Tests if the constructor handels null values properly.
   */
  @Test
  void testInvalidParametersInConstructor() {
    try {
      LOGGER.info("Testing basic with both null");
      new BasicMember(1, null, null);
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      LOGGER.info("Testing basic with enrolled date null");
      new BasicMember(2, this.ole, null);
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      LOGGER.info("Testing basic with personals null");
      new BasicMember(3, null, testDate);
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      LOGGER.info("Testing silver with both null");
      new SilverMember(1, null, null, 100000);
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      LOGGER.info("Testing silver with enrolled date null");
      new SilverMember(2, this.ole, null, 100000);
      fail();
    } catch (IllegalArgumentException expected) {
    }
    try {
      LOGGER.info("Testing silver with personals null");
      new SilverMember(3, null, testDate, 100000);
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

  /**
   * Tests if findQualificationPoints handels null values properly.
   */
  @Test
  void testInvalidParametersInFindQualificationPoints() {
    try {
      BonusMember bm = new BasicMember(1, this.ole, testDate);
      LOGGER.info("Testing qualification points method with null value");
      bm.findQualificationPoints(null);
      fail();
    } catch (IllegalArgumentException expected) {
    }
  }

}