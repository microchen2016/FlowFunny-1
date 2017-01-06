package model.victor.com;

import interfaces.victor.com.KindWoman;
import model.victor.com.PanJinLian;

/**
 * Created by victor on 2016/3/23 0023.
 * 王婆这个人老聪明了，她太老了，是个男人都看不上她，
 * 但是她有智慧经验呀，他作为一类女人的代理！
 */
public class WangPo implements KindWoman{

    private KindWoman kindWoman;

    public WangPo(){
        //默认的话是潘金莲的代理
        this.kindWoman = new PanJinLian();
    }
    //她可以是KindWomam的任何一个女人的代理，只要你是这一类型
    public WangPo(KindWoman kindWoman){
        this.kindWoman = kindWoman;
    }

    @Override
    public void makeEyesWithMan() {
        //王婆年纪大了，谁看她抛媚眼啊
        this.kindWoman.makeEyesWithMan();
    }

    @Override
    public void happyWithMan() {
        //自己老了，干不了了，但可以叫年轻的代替。
        this.kindWoman.happyWithMan();
    }
}
