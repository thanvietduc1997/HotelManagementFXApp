/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

/**
 *
 * @author Duc
 */
public class RoomManagementModelTable {
    String room_no, room_type, no_bed, price;

    public RoomManagementModelTable(String room_no, String room_type, String no_bed, String price) {
        this.room_no = room_no;
        this.room_type = room_type;
        this.no_bed = no_bed;
        this.price = price;
    }

    public String getRoom_no() {
        return room_no;
    }

    public void setRoom_no(String room_no) {
        this.room_no = room_no;
    }

    public String getRoom_type() {
        return room_type;
    }

    public void setRoom_type(String room_type) {
        this.room_type = room_type;
    }

    public String getNo_bed() {
        return no_bed;
    }

    public void setNo_bed(String no_bed) {
        this.no_bed = no_bed;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
    
    
}
