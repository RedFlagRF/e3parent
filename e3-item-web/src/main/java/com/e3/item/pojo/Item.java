package com.e3.item.pojo;

import com.e3.pojo.TbItem;

public class Item extends TbItem {
    public Item() {
    }

    public Item(TbItem tbItem) {
        this.setId(tbItem.getId());
        this.setTitle(tbItem.getTitle());
        this.setSellPoint(tbItem.getSellPoint());
        this.setPrice(tbItem.getPrice());
        this.setNum(tbItem.getNum());
        this.setBarcode(tbItem.getBarcode());
        this.setImage(tbItem.getImage());
        this.setCid(tbItem.getCid());
        this.setStatus(tbItem.getStatus());
        this.setCreated(tbItem.getCreated());
        this.setUpdated(tbItem.getUpdated());
    }

    public String[] getImages() {
        String temp = this.getImage();
        if (temp != null && !"".equals(temp)&&!"".equals(temp.trim())) {
            String[] split = temp.split(",");
            return split;
        }
        return null;
    }
}
