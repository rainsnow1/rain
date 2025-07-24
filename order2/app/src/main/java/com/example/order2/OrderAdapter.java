package com.example.order2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<Order> orderList;
    private final OrderCompleteListener listener;

    public interface OrderCompleteListener {
        void onOrderComplete(Order order);
    }

    public OrderAdapter(List<Order> orderList, OrderCompleteListener listener) {
        this.orderList = orderList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        holder.orderId.setText(String.format("#%d", order.getId()));
        holder.orderTime.setText(order.getOrderDate());
        holder.totalPrice.setText(String.format(Locale.getDefault(), "总价: ￥%.2f", order.getTotalPrice()));

        StringBuilder itemsBuilder = new StringBuilder();
        for (OrderItem item : order.getItems()) {
            itemsBuilder.append(String.format("- %s x %d\n", item.getDishName(), item.getQuantity()));
        }
        holder.orderItems.setText(itemsBuilder.toString().trim());

        holder.completeButton.setOnClickListener(v -> listener.onOrderComplete(order));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderTime, orderItems, totalPrice;
        Button completeButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.tvOrderId);
            orderTime = itemView.findViewById(R.id.tvOrderTime);
            orderItems = itemView.findViewById(R.id.tvOrderItems);
            totalPrice = itemView.findViewById(R.id.tvOrderTotalPrice);
            completeButton = itemView.findViewById(R.id.btnCompleteOrder);
        }
    }
} 