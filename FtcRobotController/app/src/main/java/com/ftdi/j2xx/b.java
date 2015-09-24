package com.ftdi.j2xx;

final class b
{
    static final byte a(final int n, final int[] array) {
        byte b = 1;
        final byte b2 = b(n, array);
        if (b2 == -1) {
            b = -1;
        }
        else {
            if (b2 == 0) {
                array[0] = 1 + (0xFFFF3FFF & array[0]);
            }
            final int a = a(array[0], array[b]);
            int n2;
            int n3;
            if (n > a) {
                n2 = -100 + n * 100 / a;
                n3 = 100 * (n % a) % a;
            }
            else {
                n2 = -100 + a * 100 / n;
                n3 = 100 * (a % n) % n;
            }
            if (n2 >= 3 && (n2 != 3 || n3 != 0)) {
                return 0;
            }
        }
        return b;
    }
    
    static byte a(final int n, final int[] array, final boolean b) {
        byte b2 = 1;
        final byte b3 = b(n, array, b);
        if (b3 == -1) {
            b2 = -1;
        }
        else {
            if (b3 == 0) {
                array[0] = 1 + (0xFFFF3FFF & array[0]);
            }
            final int a = a(array[0], array[b2], b);
            int n2;
            int n3;
            if (n > a) {
                n2 = -100 + n * 100 / a;
                n3 = 100 * (n % a) % a;
            }
            else {
                n2 = -100 + a * 100 / n;
                n3 = 100 * (a % n) % n;
            }
            if (n2 >= 3 && (n2 != 3 || n3 != 0)) {
                return 0;
            }
        }
        return b2;
    }
    
    private static int a(final int n, final int n2) {
        if (n == 0) {
            return 12000000;
        }
        if (n == 1) {
            return 8000000;
        }
        int n3 = 100 * (0xFFFF3FFF & n);
        if ((0xFFFD & n2) == 0x0) {
            switch (n & 0xC000) {
                case 49152: {
                    n3 += 12;
                    break;
                }
                case 32768: {
                    n3 += 25;
                    break;
                }
                case 16384: {
                    n3 += 50;
                    break;
                }
            }
        }
        else {
            switch (n & 0xC000) {
                case 0: {
                    n3 += 37;
                    break;
                }
                case 16384: {
                    n3 += 62;
                    break;
                }
                case 32768: {
                    n3 += 75;
                    break;
                }
                case 49152: {
                    n3 += 87;
                    break;
                }
            }
        }
        return 1200000000 / n3;
    }
    
    private static final int a(final int n, final int n2, final boolean b) {
        if (n == 0) {
            return 3000000;
        }
        int n3 = 100 * (0xFFFF3FFF & n);
        if (!b) {
            switch (0xC000 & n) {
                case 49152: {
                    n3 += 12;
                    break;
                }
                case 32768: {
                    n3 += 25;
                    break;
                }
                case 16384: {
                    n3 += 50;
                    break;
                }
            }
        }
        else if (n2 == 0) {
            switch (0xC000 & n) {
                case 16384: {
                    n3 += 50;
                    break;
                }
                case 49152: {
                    n3 += 12;
                    break;
                }
                case 32768: {
                    n3 += 25;
                    break;
                }
            }
        }
        else {
            switch (0xC000 & n) {
                case 0: {
                    n3 += 37;
                    break;
                }
                case 16384: {
                    n3 += 62;
                    break;
                }
                case 32768: {
                    n3 += 75;
                    break;
                }
                case 49152: {
                    n3 += 87;
                    break;
                }
            }
        }
        return 300000000 / n3;
    }
    
    private static byte b(final int n, final int[] array) {
        byte b = 1;
        if (n == 0) {
            b = -1;
        }
        else {
            if ((0xFFFFC000 & 12000000 / n) > 0) {
                return -1;
            }
            array[b] = 2;
            if (n >= 11640000 && n <= 12360000) {
                array[0] = 0;
                return b;
            }
            if (n >= 7760000 && n <= 8240000) {
                return (byte)(array[0] = b);
            }
            array[0] = 12000000 / n;
            array[b] = 2;
            if (array[0] == b && 100 * (12000000 % n) / n <= 3) {
                array[0] = 0;
            }
            if (array[0] != 0) {
                final int n2 = 100 * (12000000 % n) / n;
                int n3;
                if (n2 <= 6) {
                    n3 = 0;
                }
                else if (n2 <= 18) {
                    n3 = 49152;
                }
                else if (n2 <= 31) {
                    n3 = 32768;
                }
                else if (n2 <= 43) {
                    array[b] |= 0x1;
                    n3 = 0;
                }
                else if (n2 <= 56) {
                    n3 = 16384;
                }
                else if (n2 <= 68) {
                    n3 = 16384;
                    array[b] |= 0x1;
                }
                else if (n2 <= 81) {
                    n3 = 32768;
                    array[b] |= 0x1;
                }
                else if (n2 <= 93) {
                    n3 = 49152;
                    array[b] |= 0x1;
                }
                else {
                    b = 0;
                    n3 = 0;
                }
                array[0] |= n3;
                return b;
            }
        }
        return b;
    }
    
    private static byte b(final int n, final int[] array, final boolean b) {
        int n2 = 32768;
        byte b2 = 1;
        if (n == 0) {
            b2 = -1;
        }
        else {
            if ((0xFFFFC000 & 3000000 / n) > 0) {
                return -1;
            }
            array[0] = 3000000 / n;
            array[b2] = 0;
            if (array[0] == b2 && 100 * (3000000 % n) / n <= 3) {
                array[0] = 0;
            }
            if (array[0] != 0) {
                final int n3 = 100 * (3000000 % n) / n;
                if (!b) {
                    if (n3 <= 6) {
                        n2 = 0;
                    }
                    else if (n3 <= 18) {
                        n2 = 49152;
                    }
                    else if (n3 > 37) {
                        if (n3 <= 75) {
                            n2 = 16384;
                        }
                        else {
                            b2 = 0;
                            n2 = 0;
                        }
                    }
                }
                else if (n3 <= 6) {
                    n2 = 0;
                }
                else if (n3 <= 18) {
                    n2 = 49152;
                }
                else if (n3 > 31) {
                    if (n3 <= 43) {
                        array[b2] = b2;
                        n2 = 0;
                    }
                    else if (n3 <= 56) {
                        n2 = 16384;
                    }
                    else if (n3 <= 68) {
                        array[b2] = b2;
                        n2 = 16384;
                    }
                    else if (n3 <= 81) {
                        array[b2] = b2;
                    }
                    else if (n3 <= 93) {
                        n2 = 49152;
                        array[b2] = b2;
                    }
                    else {
                        b2 = 0;
                        n2 = 0;
                    }
                }
                array[0] |= n2;
                return b2;
            }
        }
        return b2;
    }
}
