Feature: A2_Tests2

  Scenario: A1_scenario
    Given a new game of quests is rigged for a1 scenario
    # 4 Stage Quest
    When player 1 draws a quest of 4 stages
    Then player 1 declines to sponsor the quest
    And player 2 accepts to sponsor the quest
    And player 2 builds the 4 stage quest with the following card positions "1,7 2,5 2,3,4 2,3"
    # Stage 1
    And player 1 decides to participate
    And player 3 decides to participate
    And player 4 decides to participate
    And player 1 discards card 1
    And player 1 builds attack with cards "5,5"
    And player 3 discards card 1
    And player 3 builds attack with cards "5,4"
    And player 4 discards card 1
    And player 4 builds attack with cards "4,6"
    # Stage 2
    And player 1 decides to participate
    And player 3 decides to participate
    And player 4 decides to participate
    And player 1 builds attack with cards "7,6"
    And player 3 builds attack with cards "9,4"
    And player 4 builds attack with cards "6,6"
    # Stage 3
    And player 3 decides to participate
    And player 4 decides to participate
    And player 3 builds attack with cards "9,6,4"
    And player 4 builds attack with cards "7,5,6"
    # Stage 4
    And player 3 decides to participate
    And player 4 decides to participate
    And player 3 builds attack with cards "7,6,6"
    And player 4 builds attack with cards "4,4,4,5"
    # End of Turn
    And player 1 continues
    And player 2 discards cards "1,1,1,1,1"
    And player 2 continues
    And player 2 continues
    And player 1's turn is over
    And player 1 should have 0 shields
    And player 1 should have cards "F5,F10,F15,F15,F30,H10,B15,B15,L20"
    And player 3 should have 0 shields
    And player 3 should have cards "F5,F5,F15,F30,S10"
    And player 4 should have 4 shields
    And player 4 should have cards "F15,F15,F40,L20"
    And player 2 should have 12 cards in their hand
