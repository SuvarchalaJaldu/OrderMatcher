package com.ordermatcher.model;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.*;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class keeps an order book, that can determine in real-time the
 * current market price and combine matching orders to trades. Each order
 * has a quantity and a price.
 *
 */
public class OrderMatcher {

  
    private List<Order> orderList = new ArrayList<>();

    /**
     * Create a new order matcher.
     */
    public OrderMatcher() {
    }
    
    /**
     * Add the specified order to the order book.
     *
     * @param order the order to be added.
     * @return any trades that were created by this order.
     */
    public List<Trade> addOrder(Order order) {
    	List<Trade> tradeList = new ArrayList<>();
        List<Order> newOrderList = new ArrayList<Order>();
        if(order.getQuantity()!=0){
            orderList.add(order);
            for(Order passiveOrder:orderList){
                if(passiveOrder.getQuantity()!=0 && order.getQuantity()!=0){
                    switch(order.getInput()){
                        case BUY:
                            if(passiveOrder.getInput().equals(Input.SELL) && order.getPrice() >= passiveOrder.getPrice()){
                                 createTrade(passiveOrder,order,newOrderList, tradeList);
                            }else{
                               newOrderList.add(passiveOrder);
                            }
                            break;
                        case SELL:
                            if(passiveOrder.getInput().equals(Input.BUY) && order.getPrice() <= passiveOrder.getPrice()){
                                createTrade(passiveOrder,order,newOrderList, tradeList);
                            }else{
                                newOrderList.add(passiveOrder);
                            }
                            break;
                    }
                }else if(order.getQuantity()==0 && passiveOrder.getQuantity()!=0){
                        newOrderList.add(passiveOrder);
                }
            }
        }
        orderList = newOrderList;
        return tradeList;
    }

    /**
     * Generate trade data and modify old order information
     *
     * @param passiveOrder the order that was already added in the list, not null.
     * @param order the order that was recently added, not null.
     * @param newOrderList the new order list to store modified orders other orders which are not modified.
     */
    private void createTrade(Order passiveOrder,Order order, List<Order> newOrderList, List<Trade> tradeList){
     
        if(passiveOrder.getQuantity()>order.getQuantity()){
            tradeList.add(new Trade(order.getId(),passiveOrder.getId(),passiveOrder.getPrice(),order.getQuantity()));
            passiveOrder.setQuantity(passiveOrder.getQuantity()-order.getQuantity());
            order.setQuantity(0);
            newOrderList.add(passiveOrder);
        }else if(passiveOrder.getQuantity()<order.getQuantity()){
            tradeList.add(new Trade(order.getId(),passiveOrder.getId(),passiveOrder.getPrice(),passiveOrder.getQuantity()));
            order.setQuantity(order.getQuantity()-passiveOrder.getQuantity());
            passiveOrder.setQuantity(0);
        }else{
            tradeList.add(new Trade(order.getId(),passiveOrder.getId(),passiveOrder.getPrice(),order.getQuantity()));
            order.setQuantity(0);
            passiveOrder.setQuantity(0);
        }
    }

    /**
     * Returns all remaining orders in the order book, in priority order, for the specified Input.
     *
     * <p>Priority for buy orders is defined as highest price, followed by time priority (first come, first served).
     * For sell orders lowest price comes first, followed by time priority (same as for buy orders).
     *
     * @param Input the Input, not null.
     * @return all remaining orders in the order book, in priority order, for the specified Input, not null.
     */
    public List<Order> getOrders(Input Input) {
        List<Order> outputList = new ArrayList<Order>();
        if(Input.equals(Input.BUY)){
            outputList = orderList.stream().filter(j -> j.getInput().equals(Input.BUY)).collect(Collectors.toList());
            Collections.sort(outputList,new Comparator<Order>(){
                @Override
                public int compare(Order o1, Order o2){
                    return Long.valueOf(o2.getPrice()).compareTo(Long.valueOf(o1.getPrice()));
                }
            });
        }else{
            outputList = orderList.stream().filter(j -> j.getInput().equals(Input.SELL)).collect(Collectors.toList());
            Collections.sort(outputList,new Comparator<Order>(){
                @Override
                public int compare(Order o1, Order o2){
                    return Long.valueOf(o1.getPrice()).compareTo(Long.valueOf(o2.getPrice()));
                }
            });
        }
        return outputList;
    }

    public static void main(String... args) throws Exception {
        OrderMatcher matcher = new OrderMatcher();
        System.out.println("WELCOME TO TRADE ORDER MATCHER");
        System.out.println();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;
        while ((line=reader.readLine()) != null) {
            line = line.trim();
            try {
                switch(line) {
                    case "PRINT":
                        System.out.println("--- BUY ---");
                        matcher.getOrders(Input.BUY).stream().map(Order::toString).forEach(System.out::println);
                        System.out.println("--- SELL ---");
                        matcher.getOrders(Input.SELL).stream().map(Order::toString).forEach(System.out::println);
                        break;
                    default:
                        matcher.addOrder(Order.parse(line)).stream().map(Trade::toString).forEach(System.out::println);
                        break;
                }
            } catch (IllegalArgumentException e) {
                System.err.println("Bad input: " + e.getMessage());
            } catch (UnsupportedOperationException e) {
                System.err.println("Sorry, this command is not supported yet: " + e.getMessage());
            }
        }
        System.out.println("Good bye!");
    }
}
