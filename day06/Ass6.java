import java.util.*;
import java.util.stream.Collectors;

public class Ass6 {

    public static void main(String[] args) {
        if (args.length != 0) {
            System.out.println(args[0].equals("1") ? partOne() : partTwo());
        } 
    }

    private static int partOne() {
        Map<String, String> map = buildRelationsMap();

        int total = 0;
        for (String value : map.keySet()) {

            //System.out.println("Key " + value);
            while (map.containsKey(value)) {
                total++;
                //System.out.println(value + " -> " + map.get(value));
                value = map.get(value);
            }
        }

        return total;
    }
 
    private static int partTwo() {
        Map<String, String> map = buildRelationsMap();

        Set<String> visited = new HashSet<>();
        Queue<Wrapper> queue =  new LinkedList<>();
        queue.add(new Wrapper("YOU", -1));

        while(!queue.isEmpty()) {
            Wrapper item = queue.remove();
            visited.add(item.getKey());

            Set<String> targets = new HashSet<>();
            targets.add(map.get(item.getKey()));
            targets.addAll(getOrbiteers(item.getKey(), map));

            if (targets.contains("SAN")) {
                //System.out.println(item.getKey());
                return item.getSteps();
            }

            for (String target : targets) {
                if (visited.contains(target)) {
                    continue;
                }

                queue.add(new Wrapper(target, item.getSteps() + 1));
            }
        }

        return -1;
    }

    private static Map<String,String> buildRelationsMap() {
        Map<String, String> result = new HashMap<>();
        
        try (Scanner sc = new Scanner(System.in)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] split = line.split("\\)");
                result.put(split[1], split[0]);
            }
        }

        return result;
    }

    private static Set<String> getOrbiteers(String key, Map<String,String> map) {
        return map.entrySet().stream().filter(o -> o.getValue().equals(key)).map(Map.Entry::getKey).collect(Collectors.toSet());
    }

    private static class Wrapper {
        private final int steps;
        private final String key;

        public Wrapper(String key, int steps) {
            this.key = key;
            this.steps = steps;
        }

        public int getSteps() {
            return steps;
        }

        public String getKey() {
            return key;
        }
    }
}