package no.simenbai.idatg2001.obligs.two;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The Member archive test.
 */
class MemberArchiveTest {
    private LocalDate testDate;
    private Personals ole;
    private MemberArchive memberArchive;
    private int oleMemberNo;

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
     * Tear down.
     */
    @AfterEach
    void tearDown() {
    }

    /**
     * Test if the addMember function works as intended.
     */
    @Test
    void addMember() {
        this.memberArchive.addMember(this.ole, testDate);
        BonusMember member = this.memberArchive.members.get(this.memberArchive.members.size()-1);
        System.out.println("addMember() - Checking points");
        assertEquals(0, member.getPoints());
        System.out.println("addMember() - Checking personals");
        assertEquals(this.ole, member.getPersonals());
        System.out.println("addMember() - Checking enrolledDate");
        assertEquals(this.testDate, member.getEnrolledDate());
        System.out.println("addMember() - Checking class");
        assertEquals(member.getClass(), BasicMember.class);
    }

    /**
     * Test if the findPoints() function works as intended
     */
    @Test
    void findPoints() {
        System.out.println("findPoints() - Testing correct member no, and password, no points");
        int points = this.memberArchive.findPoints(this.oleMemberNo, "ole");
        assertEquals(0, points);

        System.out.println("findPoints() - Testing correct member no, and wrong password");
        points = this.memberArchive.findPoints(this.oleMemberNo, "o");
        assertEquals(-1, points);

        System.out.println("findPoints() - Testing wrong member no, and correct password");
        points = this.memberArchive.findPoints(0, "ole");
        assertEquals(-1, points);

        this.memberArchive.registerPoints(this.oleMemberNo, 1000);
        System.out.println("findPoints() - Testing correct member no, and password, with points");
        points = this.memberArchive.findPoints(this.oleMemberNo, "ole");
        assertEquals(1000, points);
    }

    /**
     * Test if the registerPoints() function works as intended.
     */
    @Test
    void registerPoints() {
        System.out.println("registerPoints() - Adding 1000 points, with correct member");
        boolean response = this.memberArchive.registerPoints(this.oleMemberNo, 1000);
        int points = this.memberArchive.findPoints(this.oleMemberNo, "ole");
        assertEquals(1000, points);
        assertTrue(response);

        System.out.println("registerPoints() - Adding 1000 points, with wrong member");
        response = this.memberArchive.registerPoints(0, 1000);
        assertFalse(response);
    }

    /**
     * Check if the checkMembers() function works as intended.
     */
    @Test
    void checkMembers() {
        BonusMember member = this.memberArchive.members.get(0);

        System.out.println("checkMembers() - Testing a member with 0 points");
        this.memberArchive.checkMembers(testDate);
        assertTrue(member instanceof BasicMember);

        System.out.println("checkMembers() - Testing a member with 25000 points");
        this.memberArchive.registerPoints(this.oleMemberNo, 25000);
        this.memberArchive.checkMembers(testDate);
        member = this.memberArchive.members.get(0);
        assertTrue(member instanceof SilverMember);

        System.out.println("checkMembers() - Testing a member with over 75000 points");
        this.memberArchive.registerPoints(this.oleMemberNo, 50000);
        this.memberArchive.checkMembers(testDate);
        member = this.memberArchive.members.get(0);
        assertTrue(member instanceof GoldMember);
    }
}