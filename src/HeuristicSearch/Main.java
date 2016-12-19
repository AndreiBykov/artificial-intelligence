package HeuristicSearch;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Выберите функцию информированного поиска:");
        System.out.println("1. Манхэттенское расстояние");
        System.out.println("2. Количество фишек не на своих местах");
        Scanner scanner = new Scanner(System.in);
        switch (scanner.nextInt()) {
            case 1:
                NodeManhattanDistance.start();
                break;
            case 2:
                NodeNumberElements.start();
                break;
            default:
                System.out.println("Вы сделали неправильный выбор");

        }
    }
}
