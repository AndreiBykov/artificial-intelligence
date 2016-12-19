package BlindSearch;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        System.out.println("Выберите стратегию неинформированного поиска:");
        System.out.println("1. Поиск в глубину");
        System.out.println("2. Двунаправленный поиск");
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextInt()) {
            case 1:
                NodeDepth.start();
                break;
            case 2:
                NodeBidirectional.start();
                break;
            default:
                System.out.println("Вы сделали неправильный выбор");

        }

    }
}
