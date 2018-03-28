package com.teamtreehouse.techdegree.overboard.model;

import static org.junit.Assert.*;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class UserTest {

  Board board;
  User alice;
  User bob;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    board = new Board("Java");
    alice = board.createUser("alice");
    bob = board.createUser("bob");
  }

  @Test
  public void reputationGoesUpByFivePoints(){

    int aliceReputationBeforeVoting = alice.getReputation();

    Question question = alice.askQuestion("What is a String?");
    bob.upVote(question);
    int aliceReputationAfterVoting = alice.getReputation();
    int differenceOfPoints = aliceReputationAfterVoting - aliceReputationBeforeVoting;

    assertEquals("Reputation goes up to 5",5, differenceOfPoints);
  }

  @Test
  public void reputationGoesUpBy10ForUpvotedAnswer(){
    int bobReputationBeforeAnswered = bob.getReputation();

    Question question = alice.askQuestion("What is a String?");
    Answer answer = bob.answerQuestion(question, "An array of characters");
    alice.upVote(answer);
    int bobReputationAfterAnswered = bob.getReputation();

    assertEquals(10, (bobReputationAfterAnswered - bobReputationBeforeAnswered));
  }

  @Test
  public void reputationBoostBy15PointForAcceptedAnswer(){
    int bobReputationBeforeAnswered = bob.getReputation();

    Question question = alice.askQuestion("What is a String?");
    Answer answer = bob.answerQuestion(question, "An array of characters");
    alice.acceptAnswer(answer);
    int bobReputationAfterAnswered = bob.getReputation();

    assertEquals(15, (bobReputationAfterAnswered - bobReputationBeforeAnswered));
  }

  /**
   * User.getReputation exceed Expectation
   */
  @Test
  public void reputationDownByOnePointForDownVotedAnwser(){
    int bobReputationBeforeAnswered = bob.getReputation();

    Question question = alice.askQuestion("What is a String?");
    Answer answer = bob.answerQuestion(question, "An array of characters");
    alice.downVote(answer);
    int bobReputationAfterAnswered = bob.getReputation();

    assertEquals(-1, (bobReputationAfterAnswered - bobReputationBeforeAnswered));
  }


  @Test(expected = VotingException.class)
  public void upVoteNotAllowedByAuthorOfQuestion(){
    Question question = alice.askQuestion("What is a String?");
    alice.upVote(question);
  }

  @Test(expected = VotingException.class)
  public void upVoteNotAllowedByAuthorOfAnswer(){
    Question question = alice.askQuestion("What is a String?");
    Answer answer = bob.answerQuestion(question, "An array of characters");
    bob.upVote(answer);
  }

  @Test
  public void onlyOriginalQuestionerCanAcceptAnswer(){
    thrown.expect(AnswerAcceptanceException.class);
    String message = String.format("Only %s can accept this answer as it is their question",
        alice.getName());
    thrown.expectMessage(message);

    Question question = alice.askQuestion("What is a String?");
    Answer answer = bob.answerQuestion(question, "An array of characters");
    bob.acceptAnswer(answer);
  }
}