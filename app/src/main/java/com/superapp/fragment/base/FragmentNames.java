package com.superapp.fragment.base;

/**
 * Created by admin on 23/7/2015.
 */
public enum FragmentNames {
    FragmentProject(0),
    FragmentAddClient(1),
    FragmentDesignerDashboard(2),
    FragmentAddProject(3),
    FragmentAddCoWorker(4),
    FragmentSettings(5),
    FragEditProfile(6),
    FragmentTransactions(7),
    FragmentNotification(8),
    //    FragmentMoneyRequest(9),
    FragmentSchedule(10),
    FragmentAddCheckList(11),
    FragmentClarificationsCompany(12),
    FragmentRecommendationsCompany(13),
    //FragmentSuperSearch(14),
    FragmentTeam(15),
    FragmentApproval(16),
    FragmentRecommendations(17),
    FragmentClarifications(18),
    FragmentClientDetail(19),
    FragmentCoworkerDetail(20),
    FragmentCheckList(21),
    FragEditProfileClient(22),
    FragEditProfileCoworker(23),
    FragSSEditProfileCoworker(24),

    FragmentClientDashboard(31),

    FragDashArchivedProject(51),
    FragmentArchivedProject(52),
    FragmentCoWorkerDashboard(53),

    FragmentFeedback(61),
    FragmentAboutUs(62),
    FragmentHistory(63),
    FragDowngradeMembership(64),
    FragExpiredMembership(65),
    FragUpgradeMembership(66),

    FragmentAddMaterial(70),

    FragmentReminder(71),
    FragmentSurvey(72);


    private final int value;

    FragmentNames(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }

}
