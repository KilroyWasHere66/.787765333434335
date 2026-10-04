// ТЕМА 1. Базовый синтаксис, типы, операторы


    // 1.1
    static int dpToPx(double dp, double density) { return (int) Math.round(dp * density); }

    // 1.2
    static String parseTimestamp(long ms) {
        long seconds = ms / 1000, minutes = seconds / 60, hours = minutes / 60;
        return "полных часов: " + hours + ", минут: " + minutes + ", секунд: " + seconds
                + " (остаток: " + hours + "ч " + minutes % 60 + "м " + seconds % 60 + "с)";
    }

    // 1.3
    static double batteryHours(double capacityMah, double radioMa, double displayMa) {
        return capacityMah / (radioMa + displayMa);
    }

    // 1.4
    static boolean validCoords(double lat, double lon) {
        boolean latOk = lat >= -90.0 && lat <= 90.0;
        boolean lonOk = lon >= -180.0 && lon <= 180.0;
        boolean outOfRange = lat < -90.0 || lat > 90.0 || lon < -180.0 || lon > 180.0;
        return latOk && lonOk && !outOfRange;
    }

    // 1.5
    static final int CAMERA = 1, LOCATION = 2, STORAGE = 4;
    static int grant(int flags, int perm)  { return flags | perm; }
    static int revoke(int flags, int perm) { return flags & ~perm; }
    static boolean has(int flags, int perm){ return (flags & perm) == perm; }
    // ТЕМА 2. Управляющие конструкции

    // 2.1
    static String orientation(int w, int h) { return w == h ? "SQUARE" : (h > w ? "PORTRAIT" : "LANDSCAPE"); }

    // 2.2
    static String httpCategory(int code) {
        return switch (code / 100) {
            case 1 -> "Информационный (1xx)";
            case 2 -> "Успешный (2xx)";
            case 3 -> "Перенаправление (3xx)";
            case 4 -> "Ошибка клиента (4xx)";
            case 5 -> "Ошибка сервера (5xx)";
            default -> "Неизвестный код";
        };
    }

    // 2.3
    static void backoffDemo() {
        for (int attempt = 1; attempt <= 5; attempt++) {
            p("Попытка " + attempt + " не удалась, ждём " + (1 << (attempt - 1)) + " с");
        }
    }

    // 2.4
    static int processMessageIds(int[] ids) {
        int processed = 0;
        for (int id : ids) {
            if (id == -1) continue;
            if (id == 0) break;
            processed++;
        }
        return processed;
    }

    // 2.5
    static boolean checkPin(String correct, Iterator<String> inputs) {
        int attempts = 0;
        boolean ok = false;
        do {
            if (!inputs.hasNext()) break;
            String in = inputs.next();
            attempts++;
            ok = in.matches("\\d{4}") && in.equals(correct);
            if (!ok) p("Неверный PIN, осталось попыток: " + (3 - attempts));
        } while (!ok && attempts < 3);
        return ok;
    }

    // ТЕМА 3. Массивы и строки

    // 3.1
    static String normalizeQuery(String q) {
        return q == null ? "" : q.trim().toLowerCase().replaceAll("\\s+", " ");
    }

    // 3.2
    static void reverseInPlace(String[] a) {
        for (int i = 0, j = a.length - 1; i < j; i++, j--) {
            String t = a[i]; a[i] = a[j]; a[j] = t;
        }
    }

    // 3.3
    static String maskCard(String card) {
        String d = card.replaceAll("\\D", "");
        if (d.length() != 16) throw new IllegalArgumentException("Нужно ровно 16 цифр");
        return "**** **** **** " + d.substring(12);
    }

    // 3.4
    static double maxSpike(double[] v) {
        double max = 0;
        for (int i = 1; i < v.length; i++) max = Math.max(max, Math.abs(v[i] - v[i - 1]));
        return max;
    }

    // 3.5
    static String enc(String s) { return URLEncoder.encode(s, StandardCharsets.UTF_8); }
    static String buildQuery(String[] keys, String[] vals) {
        if (keys.length != vals.length) throw new IllegalArgumentException("Размеры массивов различаются");
        if (keys.length == 0) return "";
        StringBuilder sb = new StringBuilder("?");
        for (int i = 0; i < keys.length; i++) {
            if (i > 0) sb.append('&');
            sb.append(enc(keys[i])).append('=').append(enc(vals[i]));
        }
        return sb.toString();
    }

    // ТЕМА 4. Методы

    // 4.1
    static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@([\\w-]+\\.)+[A-Za-z]{2,}$");
    static boolean isValid(String email) { return email != null && EMAIL.matcher(email).matches(); }
    static boolean isValid(String email, boolean checkDomain) {
        if (!isValid(email)) return false;
        if (!checkDomain) return true;
        String domain = email.substring(email.indexOf('@') + 1).toLowerCase();
        return !Set.of("example.com", "test.com", "mailinator.com").contains(domain);
    }

    // 4.2
    static String formatPrice(double amount, String currencySymbol) {
        return String.format(Locale.forLanguageTag("ru-RU"), "%,.2f %s", amount, currencySymbol);
    }

    // 4.3
    static double calculateCache(long... fileSizesInBytes) {
        long sum = 0;
        for (long s : fileSizesInBytes) sum += s;
        return sum / (1024.0 * 1024.0);
    }

    // 4.4
    record Node(String name, List<Node> children) {
        Node(String name) { this(name, new ArrayList<>()); }
        Node add(Node child) { children.add(child); return this; }
    }
    static int countNested(Node dir) {
        int count = 0;
        for (Node child : dir.children()) count += 1 + countNested(child);
        return count;
    }

    // 4.5
    static int compareVersions(String v1, String v2) {
        String[] a = v1.split("\\."), b = v2.split("\\.");
        for (int i = 0; i < Math.max(a.length, b.length); i++) {
            int x = i < a.length ? Integer.parseInt(a[i]) : 0;
            int y = i < b.length ? Integer.parseInt(b[i]) : 0;
            if (x != y) return x > y ? 1 : -1;
        }
        return 0;
    }


    // ТЕМА 5. Классы и инкапсуляция


    // 5.1
    static class SettingsModel {
        private boolean isDarkMode;
        private int volumeLevel = 50;
        private String appLanguage = "ru";
        public boolean isDarkMode() { return isDarkMode; }
        public void setDarkMode(boolean v) { isDarkMode = v; }
        public int getVolumeLevel() { return volumeLevel; }
        public void setVolumeLevel(int v) {
            if (v < 0 || v > 100) throw new IllegalArgumentException("Громкость 0..100, получено " + v);
            volumeLevel = v;
        }
        public String getAppLanguage() { return appLanguage; }
        public void setAppLanguage(String l) {
            if (l == null || l.isBlank()) throw new IllegalArgumentException("Язык не задан");
            appLanguage = l;
        }
    }

    // 5.2
    record CartItem(String id, String title, double price, int count) {
        CartItem {
            if (price < 0 || count < 0) throw new IllegalArgumentException("price/count < 0");
        }
        double total() { return price * count; }
    }

    // 5.3
    static class BadgeCounter {
        private int value;
        public int get() { return value; }
        public void set(int v) {
            if (v < 0) throw new IllegalArgumentException("Счётчик не может быть < 0");
            value = v;
        }
        public void increment() { value++; }
        public void decrement() { if (value > 0) value--; }
        public void reset() { value = 0; }
    }

    // 5.4
    static class SessionTracker {
        private static final long TIMEOUT_MS = 15 * 60 * 1000L;
        private final long loginTime;
        private long lastActionTime;
        SessionTracker(long nowMs) { loginTime = nowMs; lastActionTime = nowMs; }
        void touch(long nowMs) { lastActionTime = nowMs; }
        boolean isExpired(long nowMs) { return nowMs - lastActionTime > TIMEOUT_MS; }
        long sessionLengthMs(long nowMs) { return nowMs - loginTime; }
    }

    // 5.5
    record GeoPoint(double latitude, double longitude) {
        GeoPoint {
            if (!validCoords(latitude, longitude))
                throw new IllegalArgumentException("Недопустимые координаты: " + latitude + ", " + longitude);
        }
    }

    // ТЕМА 6. Наследование и полиморфизм

    // 6.1
    static abstract class BaseScreen {
        abstract String title();
        void onOpen()  { p("[" + title() + "] onOpen"); }
        void onClose() { p("[" + title() + "] onClose"); }
    }
    static class LoginScreen extends BaseScreen {
        String title() { return "Login"; }
        @Override void onOpen() { super.onOpen(); p("  -> показать форму входа"); }
    }
    static class HomeScreen extends BaseScreen {
        String title() { return "Home"; }
        @Override void onOpen() { super.onOpen(); p("  -> загрузить ленту"); }
    }
    static class SettingsScreen extends BaseScreen {
        String title() { return "Settings"; }
        @Override void onClose() { p("  -> сохранить настройки"); super.onClose(); }
    }

    // 6.2
    static abstract class AnalyticsEvent {
        final String name;
        AnalyticsEvent(String name) { this.name = name; }
        abstract String format();
    }
    static class ClickEvent extends AnalyticsEvent {
        final String viewId;
        ClickEvent(String viewId) { super("click"); this.viewId = viewId; }
        String format() { return name + " view=" + viewId; }
    }
    static class PurchaseEvent extends AnalyticsEvent {
        final String orderId; final double sum;
        PurchaseEvent(String orderId, double sum) { super("purchase"); this.orderId = orderId; this.sum = sum; }
        String format() { return name + " order=" + orderId + " sum=" + sum + " RUB"; }
    }
    static class ScreenViewEvent extends AnalyticsEvent {
        final String screen;
        ScreenViewEvent(String screen) { super("screen_view"); this.screen = screen; }
        String format() { return name + " screen=" + screen; }
    }
    static class AnalyticsService {
        void log(AnalyticsEvent e) { p("[ANALYTICS] " + e.format()); }
    }

    // 6.3
    static abstract class DeviceSensor { abstract String readData(); }
    static class GyroscopeSensor extends DeviceSensor {
        final double x, y, z;
        GyroscopeSensor(double x, double y, double z) { this.x = x; this.y = y; this.z = z; }
        String readData() { return String.format(Locale.US, "gyro x=%.2f y=%.2f z=%.2f rad/s", x, y, z); }
    }
    static class LightSensor extends DeviceSensor {
        final double lux;
        LightSensor(double lux) { this.lux = lux; }
        String readData() { return "light " + lux + " lx"; }
    }

    // 6.4
    static abstract class UiComponent {
        protected final int id;
        protected boolean isVisible = true;
        UiComponent(int id) { this.id = id; }
        abstract void render();
    }
    static class ButtonComponent extends UiComponent {
        private final String text;
        ButtonComponent(int id, String text) { super(id); this.text = text; }
        void render() { p("Рендер кнопки [" + id + "]: '" + text + "'"); }
    }
    static class ImageComponent extends UiComponent {
        private final String url;
        ImageComponent(int id, String url) { super(id); this.url = url; }
        void render() { p("Рендер изображения [" + id + "]: " + url); }
    }
    static void drawScreen(List<UiComponent> components) {
        for (UiComponent c : components) if (c.isVisible) c.render();
    }

    // 6.5
    static abstract class Subscription {
        protected final double basePrice;
        Subscription(double basePrice) { this.basePrice = basePrice; }
        abstract double totalCost();
    }
    static class MonthlySubscription extends Subscription {
        MonthlySubscription(double base) { super(base); }
        double totalCost() { return basePrice; }
    }
    static class FamilySubscription extends Subscription {
        private final int users;
        FamilySubscription(double base, int users) {
            super(base);
            if (users < 2 || users > 6) throw new IllegalArgumentException("Семейный план: 2..6 пользователей");
            this.users = users;
        }
        double totalCost() { return basePrice * (1 + 0.5 * (users - 1)); } // каждый следующий -50%
    }
    static class AnnualDiscountSubscription extends Subscription {
        private final double discountPercent;
        AnnualDiscountSubscription(double base, double discountPercent) { super(base); this.discountPercent = discountPercent; }
        double totalCost() { return basePrice * 12 * (1 - discountPercent / 100); }
    }

    // ТЕМА 7. Интерфейсы

    // 7.1
    interface KeyValueStorage {
        void save(String key, String value);
        String get(String key);
        void clear();
    }
    static class MemoryStorage implements KeyValueStorage {
        private final Map<String, String> map = new HashMap<>();
        public void save(String k, String v) { map.put(k, v); }
        public String get(String k) { return map.get(k); }
        public void clear() { map.clear(); }
    }

    // 7.2
    interface ImageLoadCallback {
        void onSuccess(String bitmapRef);
        void onError(Throwable error);
    }

    // 7.3
    interface BackgroundTaskListener {
        void onFinished(boolean success);
        default void onProgress(int percentage) { p("Прогресс: " + percentage + "%"); }
    }

    // 7.4
    interface Playable { void play(); void stop(); }
    interface Shareable { void shareViaBluetooth(); }
    static class MediaFile implements Playable, Shareable {
        private final String name;
        MediaFile(String name) { this.name = name; }
        public void play() { p("Играет: " + name); }
        public void stop() { p("Остановлено: " + name); }
        public void shareViaBluetooth() { p("Отправка по Bluetooth: " + name); }
    }

    // 7.5
    @FunctionalInterface
    interface PredicateValidator<T> { boolean validate(T data); }

    // ТЕМА 8. Коллекции и Generics

    // 8.1
    static <T> Set<T> dedupe(List<T> list) { return new LinkedHashSet<>(list); }

    // 8.2
    static void processSyncQueue() {
        Queue<String> queue = new ArrayDeque<>();
        queue.offer("sync:contacts");
        queue.offer("sync:photos");
        queue.offer("sync:settings");
        while (!queue.isEmpty()) p("Обработка (FIFO): " + queue.poll());
    }

    // 8.3
    static class ApiResponse<T> {
        private final int statusCode;
        private final T data;
        private final String errorMessage;
        ApiResponse(int statusCode, T data, String errorMessage) {
            this.statusCode = statusCode; this.data = data; this.errorMessage = errorMessage;
        }
        public int getStatusCode() { return statusCode; }
        public T getData() { return data; }
        public String getErrorMessage() { return errorMessage; }
        public boolean isSuccessful() { return statusCode >= 200 && statusCode < 300 && errorMessage == null; }
    }

    // 8.4
    record Product(String name, double price, double rating) {}
    static List<Product> sortProducts(List<Product> list) {
        List<Product> copy = new ArrayList<>(list);
        copy.sort(Comparator.comparingDouble(Product::price)
                .thenComparing(Product::rating, Comparator.reverseOrder()));
        return copy;
    }

    // 8.5
    static class LruCache<K, V> extends LinkedHashMap<K, V> {
        private final int maxSize;
        LruCache(int maxSize) { super(16, 0.75f, true); this.maxSize = maxSize; }
        @Override protected boolean removeEldestEntry(Map.Entry<K, V> eldest) { return size() > maxSize; }
    }

    // ТЕМА 9. Исключения

    // 9.1
    static class NoInternetException extends Exception {
        NoInternetException(String msg) { super(msg); }
    }
    static String fetchData(boolean hasConnection) throws NoInternetException {
        if (!hasConnection) throw new NoInternetException("Нет подключения к сети");
        return "{\"status\":\"ok\"}";
    }

    // 9.2
    static String readConfig(Path path) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            return br.lines().collect(Collectors.joining("\n"));
        }
    }

    // 9.3
    static class InvalidUserDataException extends RuntimeException {
        InvalidUserDataException(String msg) { super(msg); }
    }
    static int parseAge(String ageStr) {
        int age;
        try { age = Integer.parseInt(ageStr.trim()); }
        catch (NumberFormatException | NullPointerException e) {
            throw new InvalidUserDataException("Возраст не является числом: " + ageStr);
        }
        if (age < 0 || age > 130) throw new InvalidUserDataException("Недопустимый возраст: " + age);
        return age;
    }

    // 9.4
    static String multiCatchDemo(int mode) {
        try {
            switch (mode) {
                case 0 -> { String s = null; s.length(); }
                case 1 -> { int[] a = new int[2]; a[5] = 1; }
                default -> throw new IllegalStateException("прочая ошибка");
            }
            return "OK";
        } catch (NullPointerException e) {
            return "NPE: обращение к null";
        } catch (IndexOutOfBoundsException e) {
            return "IOOBE: " + e.getMessage();
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }

    // 9.5
    static String safeGetString(Map<String, Object> bundle, String key, String def) {
        try {
            Object v = bundle.get(key);
            return v == null ? def : (String) v;
        } catch (Exception e) { // NPE (bundle == null), ClassCastException и др.
            return def;
        }
    }

    // ПРАКТИКА. БЛОК 1: Авторизация и валидация (P1–P10)

    // P1
    static boolean isStrongPassword(String pw) {
        return pw != null && pw.length() >= 8
                && pw.chars().anyMatch(Character::isUpperCase)
                && pw.chars().anyMatch(Character::isDigit)
                && pw.chars().anyMatch(c -> "!@#$%^&*".indexOf(c) >= 0);
    }

    // P2
    static String normalizePhone(String raw) {
        String d = raw.replaceAll("\\D", "");
        if (d.length() == 11 && (d.startsWith("8") || d.startsWith("7"))) d = "7" + d.substring(1);
        else if (d.length() == 10) d = "7" + d;
        else throw new IllegalArgumentException("Неверный номер: " + raw);
        return "+" + d;
    }

    // P3
    private static final SecureRandom RND = new SecureRandom();
    static String generateOtp() { return String.format("%06d", RND.nextInt(1_000_000)); }

    // P4
    static boolean isTokenActive(long expSec) { return isTokenActive(expSec, System.currentTimeMillis() / 1000); }
    static boolean isTokenActive(long expSec, long nowSec) { return nowSec < expSec; }

    // P5
    static String maskEmail(String email) {
        int at = email.indexOf('@');
        if (at < 1) throw new IllegalArgumentException("Некорректный email");
        String local = email.substring(0, at), domain = email.substring(at);
        if (local.length() <= 2) return "*".repeat(local.length()) + domain;
        return local.charAt(0) + "*".repeat(local.length() - 2) + local.charAt(local.length() - 1) + domain;
    }

    // P6
    static class LoginThrottler {
        static final int MAX_ATTEMPTS = 5;
        static final long LOCK_MS = 60_000;
        private final LongSupplier clock;
        private int failures;
        private long lockedUntil;
        LoginThrottler() { this(System::currentTimeMillis); }
        LoginThrottler(LongSupplier clock) { this.clock = clock; }
        boolean isBlocked() { return clock.getAsLong() < lockedUntil; }
        long remainingSeconds() { return Math.max(0, (lockedUntil - clock.getAsLong() + 999) / 1000); }
        /** @return true если вход разрешён */
        boolean tryLogin(boolean passwordCorrect) {
            if (isBlocked()) return false;
            if (passwordCorrect) { failures = 0; return true; }
            if (++failures >= MAX_ATTEMPTS) { lockedUntil = clock.getAsLong() + LOCK_MS; failures = 0; }
            return false;
        }
    }

    // P7
    static String encrypt(String text, int[] key) {
        int n = key.length;
        StringBuilder in = new StringBuilder(text);
        while (in.length() % n != 0) in.append('\u0000');
        StringBuilder out = new StringBuilder();
        for (int b = 0; b < in.length(); b += n) for (int k : key) out.append(in.charAt(b + k));
        return out.toString();
    }
    static String decrypt(String cipher, int[] key) {
        int n = key.length;
        char[] res = new char[cipher.length()];
        for (int b = 0; b < cipher.length(); b += n)
            for (int i = 0; i < n; i++) res[b + key[i]] = cipher.charAt(b + i);
        return new String(res).replaceAll("\u0000+$", "");
    }

    // P8
    enum BioStatus { READY, NO_HARDWARE, NO_PERMISSION, HW_UNAVAILABLE, NONE_ENROLLED }
    static BioStatus checkBiometric(boolean hasHardware, boolean permissionGranted,
                                    boolean hwAvailable, boolean enrolled) {
        if (!hasHardware) return BioStatus.NO_HARDWARE;
        if (!permissionGranted) return BioStatus.NO_PERMISSION;
        if (!hwAvailable) return BioStatus.HW_UNAVAILABLE;
        if (!enrolled) return BioStatus.NONE_ENROLLED;
        return BioStatus.READY;
    }

    // P9
    static class InactivityGuard {
        static final long TIMEOUT_MS = 3 * 60_000L;
        private final LongSupplier clock;
        private final Runnable onTimeout;
        private long lastActivity;
        InactivityGuard(LongSupplier clock, Runnable onTimeout) {
            this.clock = clock; this.onTimeout = onTimeout; this.lastActivity = clock.getAsLong();
        }
        void onUserInteraction() { lastActivity = clock.getAsLong(); }
        boolean check() {
            if (clock.getAsLong() - lastActivity > TIMEOUT_MS) {
                onTimeout.run();                       // сброс состояния экрана
                lastActivity = clock.getAsLong();
                return true;
            }
            return false;
        }
    }

    // P10
    static boolean isValidPromo(String code) { return code != null && code.matches("[A-Z]{4}-\\d{4}"); }

    // БЛОК 2: Списки, каталоги, кэш (P11–P20)

    // P11
    record NewsItem(long id, String title, String body) {}
    record DiffResult(List<Long> added, List<Long> changed, List<Long> removed) {}
    static DiffResult diff(List<NewsItem> oldList, List<NewsItem> newList) {
        Map<Long, NewsItem> oldMap = new LinkedHashMap<>(), newMap = new LinkedHashMap<>();
        oldList.forEach(i -> oldMap.put(i.id(), i));
        newList.forEach(i -> newMap.put(i.id(), i));
        List<Long> added = new ArrayList<>(), changed = new ArrayList<>(), removed = new ArrayList<>();
        for (NewsItem n : newList) {
            NewsItem o = oldMap.get(n.id());
            if (o == null) added.add(n.id());
            else if (!o.equals(n)) changed.add(n.id());
        }
        for (NewsItem o : oldList) if (!newMap.containsKey(o.id())) removed.add(o.id());
        return new DiffResult(added, changed, removed);
    }

    // P12
    static class PaginationHelper<T> {
        private final int pageSize;
        PaginationHelper() { this(20); }
        PaginationHelper(int pageSize) { this.pageSize = pageSize; }
        List<T> getPage(List<T> all, int page) {
            int from = (page - 1) * pageSize;
            if (page < 1 || from >= all.size()) return List.of();
            return all.subList(from, Math.min(from + pageSize, all.size()));
        }
        int totalPages(int total) { return (total + pageSize - 1) / pageSize; }
    }

    // P13
    static Map<Character, List<String>> groupByFirstLetter(List<String> names) {
        Map<Character, List<String>> map = new TreeMap<>();
        for (String n : names) {
            if (n == null || n.isBlank()) continue;
            char c = Character.toUpperCase(n.trim().charAt(0));
            map.computeIfAbsent(c, k -> new ArrayList<>()).add(n);
        }
        map.values().forEach(Collections::sort);
        return map;
    }

    // P14
    record CatalogProduct(String name, String sku, String category, double price) {}
    static List<CatalogProduct> filterCatalog(List<CatalogProduct> list, String query) {
        String q = query.trim().toLowerCase();
        return list.stream()
                .filter(c -> c.name().toLowerCase().contains(q) || c.sku().toLowerCase().contains(q))
                .toList();
    }

    // P15
    static int nextBanner(int current, int size) { return (current + 1) % size; }

    // P16
    record CartLine(String title, String category, double price, int qty) {}
    static double cartTotal(List<CartLine> lines, Map<String, Double> categoryDiscountPercent, double promoPercent) {
        double sum = 0;
        for (CartLine l : lines) {
            double disc = categoryDiscountPercent.getOrDefault(l.category(), 0.0);
            sum += l.price() * l.qty() * (1 - disc / 100);
        }
        sum *= (1 - promoPercent / 100);
        return Math.round(sum * 100) / 100.0;
    }

    // P17
    static class UndoableList<T> {
        private final List<T> items;
        private final long windowMs;
        private final LongSupplier clock;
        private T pending; private int pendingIndex; private long deadline;
        UndoableList(List<T> items, long windowMs, LongSupplier clock) {
            this.items = items; this.windowMs = windowMs; this.clock = clock;
        }
        void remove(int index) {
            pending = items.remove(index);   // предыдущее отложенное удаление становится окончательным
            pendingIndex = index;
            deadline = clock.getAsLong() + windowMs;
        }
        boolean undo() {
            if (pending == null || clock.getAsLong() >= deadline) { pending = null; return false; }
            items.add(pendingIndex, pending);
            pending = null;
            return true;
        }
        List<T> items() { return items; }
    }

    // P18
    record Chat(String title, long lastMessageTime) {}
    static List<Chat> sortChats(List<Chat> chats) {
        List<Chat> copy = new ArrayList<>(chats);
        copy.sort(Comparator.comparingLong(Chat::lastMessageTime).reversed());
        return copy;
    }

    // P19
    record GalleryFile(String name, long size) {}
    static List<List<GalleryFile>> findDuplicates(List<GalleryFile> files) {
        Map<String, List<GalleryFile>> groups = new LinkedHashMap<>();
        for (GalleryFile f : files)
            groups.computeIfAbsent(f.name() + "|" + f.size(), k -> new ArrayList<>()).add(f);
        return groups.values().stream().filter(g -> g.size() > 1).toList();
    }

    // P20
    static class ImageCache {
        static final long MAX_BYTES = 100L * 1024 * 1024;
        private final LinkedHashMap<String, Long> files = new LinkedHashMap<>();
        private long total;
        void put(String name, long size) {
            Long old = files.remove(name);
            if (old != null) total -= old;
            files.put(name, size);
            total += size;
            Iterator<Map.Entry<String, Long>> it = files.entrySet().iterator();
            while (total > MAX_BYTES && it.hasNext()) {   // удаляем самые старые
                Map.Entry<String, Long> e = it.next();
                total -= e.getValue();
                it.remove();
            }
        }
        long totalBytes() { return total; }
        Set<String> names() { return files.keySet(); }
    }

    // БЛОК 3: Сеть и офлайн (P21–P30)

    // P21
    record DeepLink(String scheme, String host, String path, Map<String, String> params) {}
    static DeepLink parseDeepLink(String url) {
        URI u = URI.create(url);
        Map<String, String> params = new LinkedHashMap<>();
        String q = u.getRawQuery();
        if (q != null) for (String pair : q.split("&")) {
            int i = pair.indexOf('=');
            String k = i < 0 ? pair : pair.substring(0, i);
            String v = i < 0 ? "" : pair.substring(i + 1);
            params.put(URLDecoder.decode(k, StandardCharsets.UTF_8), URLDecoder.decode(v, StandardCharsets.UTF_8));
        }
        return new DeepLink(u.getScheme(), u.getHost(), u.getPath(), params);
    }

    // P22
    interface NetworkCall<T> { T execute() throws IOException; }
    static <T> T withRetry(NetworkCall<T> call, int maxRetries) throws IOException {
        int retries = 0;
        while (true) {
            try { return call.execute(); }
            catch (SocketTimeoutException e) {
                if (++retries > maxRetries) throw e;
                p("Таймаут, повтор " + retries + "/" + maxRetries);
            }
        }
    }

    // P23
    record OfflineAction(String type, String targetId, String payload) {}
    static class OfflineActionQueue {
        private final Queue<OfflineAction> queue = new ArrayDeque<>();
        private final Consumer<List<OfflineAction>> sender;
        private boolean online;
        OfflineActionQueue(Consumer<List<OfflineAction>> sender) { this.sender = sender; }
        void submit(OfflineAction a) { if (online) sender.accept(List.of(a)); else queue.add(a); }
        void setOnline(boolean value) {
            online = value;
            if (online && !queue.isEmpty()) {
                List<OfflineAction> batch = new ArrayList<>(queue);
                queue.clear();
                sender.accept(batch);
            }
        }
        int pending() { return queue.size(); }
    }

    // P24
    record NoteVersion(String id, String text, long updatedAt) {}
    static NoteVersion resolveConflict(NoteVersion local, NoteVersion server, Consumer<NoteVersion> pushToServer) {
        if (server.updatedAt() > local.updatedAt()) return server;  // сервер новее -> обновить локально
        pushToServer.accept(local);                                  // иначе — перезапись на сервере
        return local;
    }

    // P25
    static double kbPerSec(long bytes, long ms) { return bytes / 1024.0 / (ms / 1000.0); }
    static double mbitPerSec(long bytes, long ms) { return bytes * 8.0 / 1_000_000 / (ms / 1000.0); }

    // P26
    static OptionalInt parseNextPage(String linkHeader) {
        Matcher m = Pattern.compile("<[^>]*[?&]page=(\\d+)[^>]*>;\\s*rel=\"next\"").matcher(linkHeader);
        return m.find() ? OptionalInt.of(Integer.parseInt(m.group(1))) : OptionalInt.empty();
    }

    // P27
    record HttpResult(int code, String body) {}
    static HttpResult conditionalGet(String clientEtag, String serverEtag, String body) {
        if (clientEtag != null && clientEtag.equals(serverEtag)) return new HttpResult(304, null);
        return new HttpResult(200, body);
    }

    // P28
    static String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        String[] units = {"KB", "MB", "GB", "TB"};
        double v = bytes;
        int i = -1;
        do { v /= 1024; i++; } while (v >= 1024 && i < units.length - 1);
        return String.format(Locale.US, "%.1f %s", v, units[i]);
    }

    // P29
    interface QuoteListener { void onQuote(String pair, double price); }
    static class QuoteFeed {
        private final List<QuoteListener> listeners = new CopyOnWriteArrayList<>();
        private final Map<String, Double> prices = new LinkedHashMap<>();
        private final Random rnd = new Random(42);
        private ScheduledExecutorService executor;
        QuoteFeed() { prices.put("USD/RUB", 90.0); prices.put("EUR/RUB", 98.0); }
        void subscribe(QuoteListener l) { listeners.add(l); }
        void tick() {
            for (Map.Entry<String, Double> e : prices.entrySet()) {
                double np = e.getValue() * (1 + (rnd.nextDouble() - 0.5) / 100);
                e.setValue(np);
                listeners.forEach(l -> l.onQuote(e.getKey(), np));
            }
        }
        void start(long intervalMs) {
            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(this::tick, 0, intervalMs, TimeUnit.MILLISECONDS);
        }
        void stop() { if (executor != null) executor.shutdownNow(); }
    }

    // P30
    static List<String> missingProfileFields(Map<String, Object> json) {
        return Stream.of("id", "name", "email").filter(k -> json.get(k) == null).toList();
    }

    // БЛОК 4: Состояние экрана и UI (P31–P40)

    // P31
    sealed interface UiState permits Loading, Success, Empty, Failure {}
    record Loading() implements UiState {}
    record Success(List<String> data) implements UiState {}
    record Empty() implements UiState {}
    record Failure(String message) implements UiState {}
    static String render(UiState state) {
        return switch (state) {
            case Loading l -> "Показать ProgressBar";
            case Success s -> "Показать список из " + s.data().size() + " элементов";
            case Empty e -> "Показать заглушку «Ничего нет»";
            case Failure f -> "Показать ошибку: " + f.message();
        };
    }

    // P32
    static class ClickDebouncer {
        private final long intervalMs;
        private final LongSupplier clock;
        private long last = Long.MIN_VALUE / 2;
        ClickDebouncer(long intervalMs, LongSupplier clock) { this.intervalMs = intervalMs; this.clock = clock; }
        ClickDebouncer(LongSupplier clock) { this(500, clock); }
        boolean click(Runnable action) {
            long now = clock.getAsLong();
            if (now - last < intervalMs) return false;
            last = now;
            action.run();
            return true;
        }
    }

    // P33
    static class BackStack<S> {
        private final Deque<S> stack = new ArrayDeque<>();
        void push(S screen) { stack.addFirst(screen); }
        S pop() { return stack.pollFirst(); }
        void popToRoot() { while (stack.size() > 1) stack.pollFirst(); }
        S peek() { return stack.peekFirst(); }
        int size() { return stack.size(); }
    }

    // P34
    enum ThemePalette {
        LIGHT("#FFFFFF", "#F2F2F7", "#1C1B1F", "#6750A4"),
        DARK("#121212", "#1E1E1E", "#E6E1E5", "#D0BCFF");
        final String background, surface, textPrimary, accent;
        ThemePalette(String bg, String surface, String text, String accent) {
            this.background = bg; this.surface = surface; this.textPrimary = text; this.accent = accent;
        }
        static ThemePalette of(boolean darkMode) { return darkMode ? DARK : LIGHT; }
    }

    // P35
    static int profileProgress(String avatar, String bio, String phone, String email, String city) {
        String[] fields = {avatar, bio, phone, email, city};
        long filled = Arrays.stream(fields).filter(s -> s != null && !s.isBlank()).count();
        return (int) (filled * 100 / fields.length);
    }

    // P36
    static String textColorFor(int r, int g, int b) {
        double yiq = (r * 299 + g * 587 + b * 114) / 1000.0;
        return yiq >= 128 ? "#000000" : "#FFFFFF";
    }

    // P37
    static String formatCount(long n) {
        if (n < 1000) return String.valueOf(n);
        if (n < 1_000_000) return shortNum(n / 1000.0) + "K";
        return shortNum(n / 1_000_000.0) + "M";
    }
    private static String shortNum(double v) {
        String s = String.format(Locale.US, "%.1f", Math.floor(v * 10) / 10);
        return s.endsWith(".0") ? s.substring(0, s.length() - 2) : s;
    }

    // P38
    static class DialogManager {
        private final Queue<String> queue = new ArrayDeque<>();
        private String current;
        void request(String dialog) { queue.add(dialog); showNext(); }
        void dismiss() { current = null; showNext(); }
        private void showNext() {
            if (current != null || queue.isEmpty()) return;
            current = queue.poll();
            p("Показан диалог: " + current);
        }
    }

    // P39
    static boolean canPay(int cartSize, String paymentMethod, boolean addressConfirmed) {
        return cartSize > 0 && paymentMethod != null && !paymentMethod.isBlank() && addressConfirmed;
    }

    // P40
    static String translateError(Throwable t) {
        if (t instanceof UnknownHostException) return "Нет подключения к интернету. Проверьте сеть.";
        if (t instanceof SocketTimeoutException || t instanceof TimeoutException)
            return "Сервер слишком долго отвечает. Попробуйте позже.";
        if (t instanceof SSLException) return "Не удалось установить защищённое соединение.";
        if (t instanceof FileNotFoundException) return "Запрошенные данные не найдены.";
        if (t instanceof IOException) return "Ошибка обмена данными. Попробуйте ещё раз.";
        return "Что-то пошло не так. Мы уже разбираемся.";
    }

    // БЛОК 5: Железо, гео, фон (P41–P50)

    // P41 гаверсинус, метры
    static double haversine(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6_371_000;
        double p1 = Math.toRadians(lat1), p2 = Math.toRadians(lat2);
        double dp = Math.toRadians(lat2 - lat1), dl = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dp / 2) * Math.sin(dp / 2)
                + Math.cos(p1) * Math.cos(p2) * Math.sin(dl / 2) * Math.sin(dl / 2);
        return 2 * R * Math.asin(Math.sqrt(a));
    }

    // P42
    static boolean inGeofence(double lat, double lon, double cLat, double cLon, double radiusM) {
        return haversine(lat, lon, cLat, cLon) <= radiusM;
    }

    // P43
    static long gpsIntervalMs(int batteryPercent) {
        if (batteryPercent > 50) return 5_000;
        if (batteryPercent >= 15) return 30_000;
        return 300_000;
    }

    // P44
    static double magnitude(double x, double y, double z) { return Math.sqrt(x * x + y * y + z * z); }
    static boolean detectFall(double[][] samples) {
        final double FREEFALL = 3.0, IMPACT = 25.0;
        final int MAX_GAP = 10;
        int lastFree = -1;
        for (int i = 0; i < samples.length; i++) {
            double m = magnitude(samples[i][0], samples[i][1], samples[i][2]);
            if (m < FREEFALL) lastFree = i;
            else if (m > IMPACT && lastFree >= 0 && i - lastFree <= MAX_GAP) return true;
        }
        return false;
    }

    // P45
    static int countSteps(double[] a, double threshold, int minGap) {
        int steps = 0, last = -minGap;
        for (int i = 1; i < a.length - 1; i++) {
            boolean peak = a[i] > threshold && a[i] > a[i - 1] && a[i] >= a[i + 1];
            if (peak && i - last >= minGap) { steps++; last = i; }
        }
        return steps;
    }

    // P46
    static int luxToBrightness(double lux) {
        if (lux <= 0) return 0;
        double pct = 100 * Math.log10(1 + lux) / Math.log10(1 + 10_000);
        return (int) Math.max(0, Math.min(100, Math.round(pct)));
    }

    // P47
    static boolean canStartHeavySync(boolean onWifi, boolean charging) { return onWifi && charging; }

    // P48
    static class TrafficMonitor {
        static final long LIMIT_BYTES = 5L * 1024 * 1024 * 1024;
        private long mobile, wifi;
        private boolean warned;
        private final Consumer<String> warning;
        TrafficMonitor(Consumer<String> warning) { this.warning = warning; }
        void add(boolean isWifi, long bytes) {
            if (isWifi) { wifi += bytes; return; }
            mobile += bytes;
            if (!warned && mobile >= LIMIT_BYTES) {
                warned = true;
                warning.accept("Лимит мобильного трафика 5 ГБ достигнут!");
            }
        }
        long mobileBytes() { return mobile; }
        long wifiBytes() { return wifi; }
    }

    // P49
    enum PlayerState { IDLE, INITIALIZED, PREPARED, PLAYING, PAUSED, STOPPED }
    static class AudioPlayer {
        private static final Map<PlayerState, Set<PlayerState>> ALLOWED = new EnumMap<>(PlayerState.class);
        static {
            ALLOWED.put(PlayerState.IDLE, EnumSet.of(PlayerState.INITIALIZED));
            ALLOWED.put(PlayerState.INITIALIZED, EnumSet.of(PlayerState.PREPARED, PlayerState.IDLE));
            ALLOWED.put(PlayerState.PREPARED, EnumSet.of(PlayerState.PLAYING, PlayerState.STOPPED, PlayerState.IDLE));
            ALLOWED.put(PlayerState.PLAYING, EnumSet.of(PlayerState.PAUSED, PlayerState.STOPPED, PlayerState.IDLE));
            ALLOWED.put(PlayerState.PAUSED, EnumSet.of(PlayerState.PLAYING, PlayerState.STOPPED, PlayerState.IDLE));
            ALLOWED.put(PlayerState.STOPPED, EnumSet.of(PlayerState.PREPARED, PlayerState.IDLE));
        }
        private PlayerState state = PlayerState.IDLE;
        PlayerState state() { return state; }
        private void to(PlayerState next) {
            if (!ALLOWED.get(state).contains(next))
                throw new IllegalStateException("Недопустимый переход " + state + " -> " + next);
            state = next;
        }
        void setDataSource() { to(PlayerState.INITIALIZED); }
        void prepare() { to(PlayerState.PREPARED); }
        void start()   { to(PlayerState.PLAYING); }
        void pause()   { to(PlayerState.PAUSED); }
        void stop()    { to(PlayerState.STOPPED); }
        void reset()   { to(PlayerState.IDLE); }
    }

    // P50
    static String buildCrashReport(Throwable t, String androidVersion, String model, long freeBytes) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return String.join("\n",
                "=== CRASH REPORT ===",
                "Время: " + Instant.now(),
                "Android: " + androidVersion,
                "Устройство: " + model,
                "Свободно на накопителе: " + formatBytes(freeBytes),
                "--- Stacktrace ---",
                sw.toString());
    }