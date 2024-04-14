import java.util.*;

/* User.java
 *
 * This class represents a user in a social network. The code uses a static
 * HashMap to keep track of all users. The HashMap is keyed by the user's name
 * and can be searched using the find method.
 *
 * Users can:
 *  - friend another user
 * - unfriend another user
 * - leave the social network
 * - check if they are friends with another user
 */
public class User {
  /* static HashMap to keep track of all users. Static means that there is only
   * one copy of the HashMap for all instances of the class.
   */
  public static HashMap<String,User> users = new HashMap<String,User>();
  public String name;
  public HashMap<String,User> adj = new HashMap<String,User>();

  public HashMap<String,User> adjFollow = new HashMap<String,User>();

  /* Constructor for the User class. The constructor takes a String, nm, which
   * is the name of the user. The constructor adds the user to the static
   * HashMap. Warning: if a user with the same name already exists, the
   * constructor will not add the new user to the HashMap.
   */
  public User( String nm ) {
    name = nm;
    if (!users.containsKey(name)) {
      users.put(name, this);
    }
  }

  /* find
   * Given a String, nm, this method returns the User with that name. If no
   * such user exists, the method returns null.
   */
  public static User find( String nm ) {
    return users.get(nm);
  }

  /* friend
   * Given a String, f, this method will friend the user with that name. The
   * method returns the User that was friended. Friending adds the friendship to
   * adj and to the other user's adj. Friending a user that is already a friend
   * does not change the friendship.
   *
   */

  /**
   * The updated version of this method throws the necessary exception when the
   * friends command is followed by an incorrect string. It also removes the
   * "follows" relationship when users become friends.
   * */

  public User friend( String f ) throws InputMismatchException {

    if(find(f) == null){
      throw new InputMismatchException ("Invalid line: " + this.name + " friends " + f);
    }

    User u = users.get( f );

    if(this.doesFollow(u) || u.doesFollow(this)){
      this.adjFollow.remove(u.name);
      u.adjFollow.remove(this.name);
    }

    adj.put( u.name, u );
    u.adj.put( name, this );
    return u;
  }

  /**
  * Method to establish a relationship where one user follows another.
   * @param f is the name of the user to follow
   * @return u is the user who is following f
  * This method ends if the users are already friends and throws the appropriate
   * exception when the given string is incorrect.
  * */
  public User follow( String f ) throws InputMismatchException{

    if(find(f) == null){
      throw new InputMismatchException ("Invalid line: " + this.name + " follows " + f);
    }

    User u = users.get( f );

    if(this.isFriend(u)){
      return u;
    }

    adjFollow.put( u.name, u );
    return u;
  }

  /* unfriend
   * Given a String, f, this method will unfriend the user with that name. The
   * method returns the User that was unfriended. Unfriending removes the
   * friendship from adj and from the other user's adj.
   */

  /**
  * The updated version of this method throws the necessary exception when the
  * unfriends command is followed by an incorrect string. It also removes the
  * "follows" relationship if a user unfriends another.
  * */

  public User unfriend( String f ) throws InputMismatchException{

    if(find(f) == null){
      throw new InputMismatchException ("Invalid line: " + this.name + " unfriends " + f);
    }

    User u = users.get( f );
    adj.remove( u.name );
    u.adj.remove( this.name );
    this.adjFollow.remove(u.name);

    return u;
  }

  /* leave
   * This method removes the user from the social network. It removes the user
   * from the static HashMap and removes the user from all of their friends'
   * adj.
   */

  /**
   * The updated method eliminates all the "follows"
   * relationships of the departing user.
   * */
  public void leave() {
    users.remove( name );
    for( User v : adj.values() ) {
      v.adj.remove( name );
    }

    for (User w : adjFollow.values() ){
      w.adjFollow.remove( name );
    }
  }

  /* isFriend
   * Given a User, u, this method returns true if u is a friend of this user and
   * false otherwise.
   */
  public boolean isFriend( User u ) {
    return adj.containsKey( u.name );
  }

  /** Method to determine whether this user follows the parameter
   * user
   * @param u The user we are checking this user follows
   * @return a boolean value indicating if this user follows
   *         the parameter user or not.
   */
  public boolean doesFollow( User u ) {
    return adjFollow.containsKey( u.name );
  }

}
