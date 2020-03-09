package no.simenbai.idatg2001.obligs.four;

import javafx.application.Application;

import java.lang.reflect.Member;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

/**
 * The type Member archive.
 */
public class MemberArchive {
    /**
     * The Silver limit.
     */
    static final int SILVER_LIMIT = 25000;
    /**
     * The Gold limit.
     */
    static final int GOLD_LIMIT = 75000;
    /**
     * Random number.
     */
    static final Random RANDOM_NUMBER = new Random();
    /**
     * The Members.
     */
    ArrayList<BonusMember> members;

    /**
     * Instantiates a new Member archive.
     *
     * @param members the members
     */
    public MemberArchive(ArrayList<BonusMember> members) {
        this.members = members;
    }

    /**
     * Instantiates a new Member archive.
     */
    public MemberArchive(){
        this.members = new ArrayList<>();
    }

    /**
     * Add member
     *
     * @param personals the personals
     * @param date      the date
     * @return the member number
     */
    public int addMember(Personals personals, LocalDate date) {
        int memberNo = findAvailableNumber();
        members.add(new BasicMember(memberNo, personals, date));
        return memberNo;
    }

    /**
     * Find points, -1 if it could not be found
     *
     * @param memberNo the member no
     * @param password the password
     * @return points
     */
    public int findPoints(int memberNo, String password) {
        BonusMember member = findMember(memberNo);
        if (member != null && member.okPassword(password)) {
            return member.getPoints();
        }
        return -1;
    }

    /**
     * Register points returns true if successful
     *
     * @param memberNo the member no
     * @param points   the points
     * @return the boolean
     */
    public boolean registerPoints(int memberNo, int points) {
        BonusMember member = findMember(memberNo);
        if (member != null) {
            member.registerPoints(points);
            return true;
        }
        return false;
    }

    /**
     * Check if members are eligible for upgrades.
     *
     * @param date the date
     */
    public void checkMembers(LocalDate date) throws IllegalArgumentException{
        for (int i = 0; i < members.size(); i++) {
            BonusMember member = members.get(i);
            int qualifyingPoints = member.findQualificationPoints(date);
            if (qualifyingPoints >= GOLD_LIMIT && !(member instanceof GoldMember)) {
                members.set(i,
                        new GoldMember(
                                member.getMemberNo(),
                                member.getPersonals(),
                                member.getEnrolledDate(),
                                member.getPoints()
                        )
                );
            } else if (qualifyingPoints >= SILVER_LIMIT && !(member instanceof SilverMember)) {
                members.set(i,
                        new SilverMember(
                                member.getMemberNo(),
                                member.getPersonals(),
                                member.getEnrolledDate(),
                                member.getPoints()
                        )
                );
            }
        }
    }

    private BonusMember findMember(int memberNo) {
        for (BonusMember member : members) {
            if (member.getMemberNo() == memberNo) {
                return member;
            }
        }
        return null;
    }

    private int findAvailableNumber() {
        while (true) {
            int memberNo = RANDOM_NUMBER.nextInt();
            if (findMember(memberNo) == null) {
                return memberNo;
            }
        }
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // write your code here
        Gui gui = new Gui();
        gui.startGui("");
    }
}
