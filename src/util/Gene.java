/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author usuario.local
 */
public class Gene {
    public static byte[] geraCromossomo(int tamanho) {
        Random r = new Random();
        byte[] b = new byte[tamanho];
        for (int i = 0; i < tamanho; i++) {
            b[i] = (byte) r.nextInt(2);
        }
        return b;
    }
    
    public static ArrayList<byte[]> geraCromossomos(int tamanho, int populacao) {
        ArrayList<byte[]> p = new ArrayList<>();
        
        for (int i = 0; i < populacao; i++) {
            p.add(geraCromossomo(tamanho));
        }
        
        return p;
    }
}
