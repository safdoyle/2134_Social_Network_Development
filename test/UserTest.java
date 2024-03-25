import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class UserTest {
  private static String[] friendNames = {"Alice", "Bob", "Carol", "Dave", "Eve", "Frank", "Grace"};

  // clear the static users HashMap before each test
  @AfterEach
  void clearUsers() {
    User.users.clear();
  }

  // Test constructing a user adds them to the HashMap
  @Test
  void testConstructor() {
    User u = new User(friendNames[0]);
    assertEquals(friendNames[0], u.name, "Incorrect name");
    assertEquals(1, User.users.size(), "Incorrect size");
    assertEquals(u, User.users.get(friendNames[0]), "User not in HashMap");
  }

  /* Constructing two users with the same name should
   * add the first user to the HashMap but not the second.
   */
  @Test
  void testConstructorSameName() {
    User u = new User(friendNames[0]);
    User v = new User(friendNames[0]);
    assertEquals(friendNames[0], u.name, "Incorrect name");
    assertEquals(friendNames[0], v.name, "Incorrect name");
    assertEquals(1, User.users.size(), "Incorrect size");
    assertEquals(u, User.users.get(friendNames[0]), "User not in HashMap");
  }

  // Test constructing two users with different names adds both to the HashMap
  @Test
  void testConstructorDifferentName() {
    User u = new User(friendNames[0]);
    User v = new User(friendNames[1]);
    assertEquals(friendNames[0], u.name, "Incorrect name for u");
    assertEquals(friendNames[1], v.name, "Incorrect name for v");
    assertEquals(2, User.users.size(), "Incorrect size");
    assertEquals(u, User.users.get(friendNames[0]), "User u not in HashMap");
    assertEquals(v, User.users.get(friendNames[1]), "User v not in HashMap");
  }

  // Test finding a user that exists returns that user
  @Test
  void testFind() {
    User u = new User(friendNames[0]);
    User v = User.find(friendNames[0]);
    assertEquals(u, v, "find returned the wrong user");
  }

  // Test finding a user that does not exist returns null
  @Test
  void testFindNotExists() {
    User u = User.find(friendNames[0]);
    assertEquals(null, u, "find returned a user that does not exist");
  }

  // Test find returns the correct user when there are multiple users
  @Test
  void testFindMultiple() {
    User u = new User(friendNames[0]);
    User v = new User(friendNames[1]);
    User w = new User(friendNames[2]);
    User x = User.find(friendNames[1]);
    assertEquals(u, User.find(friendNames[0]), "find returned the wrong user for u");
    assertEquals(v, User.find(friendNames[1]), "find returned the wrong user for v");
    assertEquals(w, User.find(friendNames[2]), "find returned the wrong user for w");
  }

  // Test friending a user adds them to both users' adj
  @Test
  void testFriend() {
    User u = new User(friendNames[0]);
    User v = new User(friendNames[1]);
    User w = u.friend(friendNames[1]);
    assertEquals(1, u.adj.size(), "u's adj has the wrong size");
    assertEquals(1, v.adj.size(), "v's adj has the wrong size");
    assertEquals(v, u.adj.get(friendNames[1]), "u's adj has the wrong user");
    assertEquals(u, v.adj.get(friendNames[0]), "v's adj has the wrong user");
    assertEquals(v, w, "friend returned the wrong user");
  }

  // Test friending a user twice does not add them twice
  @Test
  void testFriendTwice() {
    User u = new User(friendNames[0]);
    User v = new User(friendNames[1]);
    User w = u.friend(friendNames[1]);
    User x = u.friend(friendNames[1]);
    assertEquals(1, u.adj.size(), "u's adj has the wrong size");
    assertEquals(1, v.adj.size(), "v's adj has the wrong size");
    assertEquals(v, u.adj.get(friendNames[1]), "u's adj has the wrong user");
    assertEquals(u, v.adj.get(friendNames[0]), "v's adj has the wrong user");
    assertEquals(v, w, "friend returned the wrong user");
    assertEquals(v, x, "friend returned the wrong user");
  }

  // Test unfriending a user removes them from both users' adj
  @Test
  void testUnfriend() {
    User u = new User(friendNames[0]);
    User v = new User(friendNames[1]);
    u.friend(friendNames[1]);
    User w = u.unfriend(friendNames[1]);
    assertEquals(0, u.adj.size(), "u's adj has the wrong size");
    assertEquals(0, v.adj.size(), "v's adj has the wrong size");
    assertEquals(null, u.adj.get(friendNames[1]), "u's adj has the wrong user");
    assertEquals(null, v.adj.get(friendNames[0]), "v's adj has the wrong user");
    assertEquals(v, w, "unfriend returned the wrong user");
  }

  // Test unfriending a user in the opposite order
  @Test
  void testUnfriendOpposite() {
    User u = new User(friendNames[0]);
    User v = new User(friendNames[1]);
    u.friend(friendNames[1]);
    User w = v.unfriend(friendNames[0]);
    assertEquals(0, u.adj.size(), "u's adj has the wrong size");
    assertEquals(0, v.adj.size(), "v's adj has the wrong size");
    assertEquals(null, u.adj.get(friendNames[1]), "u's adj has the wrong user");
    assertEquals(null, v.adj.get(friendNames[0]), "v's adj has the wrong user");
    assertEquals(u, w, "unfriend returned the wrong user");
  }

  // Test a single user leaving removes them from the HashMap
  @Test
  void testLeaveSingle() {
    User u = new User(friendNames[0]);
    u.leave();
    assertEquals(0, u.adj.size(), "u's adj has the wrong size");
    assertEquals(0, User.users.size(), "Incorrect size");
    assertEquals(null, User.users.get(friendNames[0]), "User not removed from HashMap");
  }

  /* Test a user leaving when they have a friend removes them from the HashMap
   * and removes them from their friend's adj.
   */
  @Test
  void testLeaveFriend() {
    User u = new User(friendNames[0]);
    User v = new User(friendNames[1]);
    u.friend(friendNames[1]);
    u.leave();
    assertEquals(0, v.adj.size(), "v's adj has the wrong size");
    assertFalse(v.adj.containsKey(friendNames[0]), "v's adj has the wrong user");
    assertEquals(1, User.users.size(), "Incorrect size");
    assertEquals(null, User.users.get(friendNames[0]), "User not removed from HashMap");
    assertEquals(v, User.users.get(friendNames[1]), "User v not in HashMap");
  }

  // Test isFriend returns true when the users are friends
  @Test
  void testIsFriendTrue() {
    User u = new User(friendNames[0]);
    User v = new User(friendNames[1]);
    u.friend(friendNames[1]);
    assertTrue(u.isFriend(v), "isFriend returned false");
  }

  // Test isFriend returns false when the users are not friends
  @Test
  void testIsFriendFalse() {
    User u = new User(friendNames[0]);
    User v = new User(friendNames[1]);
    assertFalse(u.isFriend(v), "isFriend returned true");
  }

}