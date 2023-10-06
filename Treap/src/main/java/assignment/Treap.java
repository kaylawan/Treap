package assignment;
import java.util.Iterator;

/**
 * A treap is a form of k-v tree map which relies on randomly generated
 * priorities to stay balanced.
 */
public interface Treap<K extends Comparable<K>, V> extends Iterable<K> {

    /**
     * The maximum priority that a node can have.
     */
    public static final int MAX_PRIORITY = 65535;

    /**
     * Retrieves the v associated with a k in this dictionary.
     * If the k is null or the k is not present in this
     * dictionary, this method returns null.
     *
     * @param k   the k whose associated v
     *              should be retrieved
     * @return      the v associated with the k, or
     *              null if the k is null or the k is not in
     *              this treap
     */
    V lookup(K k);

    /**
     * Adds a k-v pair to this dictionary.  Any old v
     * associated with the k is lost.  If the k or the v is
     * null, the pair is not added to the dictionary.
     *
     * @param k      the k to add to this dictionary
     * @param v    the v to associate with the k
     */
    void insert(K k, V v);

    /**
     * Removes a k from this dictionary.  If the k is not present
     * in this dictionary, this method does nothing.  Returns the
     * v associated with the removed k, or null if the k
     * is not present.
     *
     * @param k      the k to remove
     * @return         the associated with the removed k, or null
     */
    V remove(K k);

    /**
     * Splits this treap into two treaps.  The left treap should contain
     * vs less than the k, while the right treap should contain
     * vs greater than or equal to the k.
     *
     * @param k    the k to split the treap with
     * @return       the left treap in index 0, the right in index 1
     */
    Treap<K, V> [] split(K k);

    /**
     * Joins this treap with another treap.  If the other treap is not
     * an instance of the implementing class, both treaps are unmodified.
     * At the end of the join, this treap will contain the result.
     * This method may destructively modify both treaps.
     *
     * @param t    the treap to join with
     */
    void join(Treap<K, V> t);

    /**
     * Melds this treap with another treap.  If the other treap is not
     * an instance of the implementing class, both treaps are unmodified.
     * At the end of the meld, this treap will contain the result.  This
     * method may destructively modify both treaps.
     *
     * If you don't implement this method, just throw an
     * UnsupportedOperationException.
     *
     * @param t    the treap to meld with.  t may be modified.
     */
    void meld(Treap<K, V> t) throws UnsupportedOperationException;

    /**
     * Removes elements from another treap from this treap.  If the
     * other treap is not an instance of the implementing class,
     * both treaps are unmodified.  At the end of the difference,
     * this treap will contain no ks that were in the other
     * treap.  This method may destructively modify both treaps.
     *
     * If you don't implement this method, just throw an
     * UnsupportedOperationException.
     *
     * @param t   a treap containing elements to remove from this
     *            treap.  t may be destructively modified.
     */
    void difference(Treap<K, V> t) throws UnsupportedOperationException;


    /**
     * Build a human-readable version of the treap.
     * Each node in the treap will be represented as
     *
     *     [priority] <k, v>\n
     *
     * Subtreaps are indented one tab over from their parent for
     * printing.  This method prints out the string representations
     * of k and v using the object's toString(). Treaps should
     * be printed in pre-order traversal fashion.
     */
    String toString();


    /**
     * @return a fresh iterator that points to the first element in
     * this Treap and iterates in sorted order.
     */
    Iterator<K> iterator();

    /**
     * Returns the balance factor of the treap.  The balance factor
     * is the height of the treap divided by the minimum possible
     * height of a treap of this size.  A perfectly balanced treap
     * has a balance factor of 1.0.  If this treap does not support
     * balance statistics, throw an exception.
     *
     * If you don't implement this method, just throw an
     * UnsupportedOperationException.
     */
    double balanceFactor() throws UnsupportedOperationException;
}
