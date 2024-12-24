package org.example;

public class stack<T> {

    private class node {
        T data;
        node next;

        node(T _data) {
            this.data = _data;
            this.next = null;
        }

        node(T _data, node _next) {
            this.data = _data;
            this.next = _next;
        }
    }

    private node head;

    public stack() { head = null; }

    public boolean isEmpty() { return head == null; }

    public void push (T data) { head = new node(data, head); }

    public T peek() {
        if (!isEmpty()) return head.data;
        else throw new org.example.EmptyStackException("Стек не содержит элементов.");
    }

    public T pop () {
        if (head != null) {
            node tmpr = head;
            head = head.next;
            return tmpr.data;
        }
        else throw new EmptyStackException("Стек не содержит элементов.");
    }
}

