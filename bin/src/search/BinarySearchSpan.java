package search;

public class BinarySearchSpan {
    // n = a.length
    //PRE: 1 <= i <= n - 1, a[i] <= a[i - 1]
    public static int iterativeBinarySearch(int x, int[] a) {
        int l = -1;
        int r = a.length;
        while (r - l > 1) {
            // INV: r - l > 1 && l' >= l && r' <= r && x' = x && a[i'] = a[i] &&
            // a[i] >= a[i+1], l <= i < r &&
            // (l = -1 || 0 =< l < r && a[l] >= x) && (r = n || l < r < n && a[r] < x)

            int m = (l + r)/2;
            // l < m < r
            if (a[m] >= x) {
                l = m;
                // for all i l <= i < m, a[i] >= x
                // l' = m && a[l'] >= x &&
                // r' = r && (r' = n || l' < r' < n && a[r'] < x)
                // a[i'] = a[i] && x' = x
            } else {
                r = m;
                // for all i m <= i < r, a[i] < x
                // r' = m && a[r'] < x
                // l' = l && (l' = -1 || 0 <= l' < r' && a[l'] >= x)
                // a[i'] = a[i] && x = x'
            }
        }
        return l;
        // POST: -1 <= answer < n &&
        // && a[i'] = a[i] && x = x' &&
        // && (answer = n - 1, a[n - 1] >= x || answer = -1, a[0] < x ||
        // -1 < answer < n - 1, a[answer] >= x && a[answer + 1] < x)
    }
    // n = a.length

    // PRE: a[i] >= a[i + 1], for all 0 <= i < n - 1 &&
    // -1 <= l < r <= n &&
    // (l == -1 || 0 <= l < r && a[l] > x) && (r == n || l < r < n && a[r] <= x)
    public static int recursiveBinarySearch(int x, int[] a, int l, int r) {
        // a[i] >= a[i + 1], l <= i < r && ( -1 <= l < r <= n)
        // a[i'] == a[i] && x' = x
        // (l == -1 || 0 <= l < r && a[l] > x) && (r == n || l < r < n && a[r] <= x)
        if (r <= l + 1) {
            // r <= l + 1
            return r;
            // (answer = n, a[n - 1] > x || answer = 0, a[0] < x || 0 < answer < n && a[answer] <= x && a[answer - 1] > x)
        } else {
            int m = (l + r)/2;
            // l < m < r
            if (a[m] <= x) {
                return recursiveBinarySearch(x, a, l, m);
                // for all i m <= i < r, a[i] <= x
                // r' = m && a[r'] <= x
                // l' = l && l' < m && (l' = -1 || 0 <= l < r' && a[l'] > x)
                // a[i'] = a[i] && x' = x
            } else {
                return recursiveBinarySearch(x, a, m, r);
                // for all i l <= i < m, a[i] > x
                // r' = r && r' > m && (r' = n || -1 <= l < r' < n && a[r'] <= x)
                // l' = m && a[l'] > x
                // a[i'] = a[i] && x' = x
            }
        }
        // POST: l < answer <= r && a[i] >= a[i+1]
        // a[i'] = a[i] && x' = x &&
        // (answer = n, a[n - 1] > x || answer = 0, a[0] < x || 0 < answer < n && a[answer] <= x && a[answer - 1] > x)
    }

    // PRE:
    // args[0] = (String) x
    // 1 <= i < args.length - 1, int(args[i]) >= int(args[i+1])
    // POST: l >= 0, a[l] <= x && a[l - 1] > x && (for all l <= i < l + span, a[i] = x ||
    // a[l] < x && a[l - 1] > x && span = 0)
    public static void main(String[] args) {
        int x = Integer.parseInt(args[0]);
        int[] a = new int[args.length - 1];
        for (int i = 0; i < args.length - 1; i++) {
            a[i] = Integer.parseInt(args[i + 1]);
        }
        int left = recursiveBinarySearch(x, a, -1, a.length);
        int right = iterativeBinarySearch(x , a);
        if (x == Integer.MIN_VALUE) {
            System.out.println(left + " " + (a.length - left));
        } else {
            System.out.println(left + " " + (right - left + 1));
        }
    }
}