/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excecoes;

/**
 *
 * @author guard
 */
public class IsNotArrayListException extends Exception {
    public IsNotArrayListException() {
        super("O objeto não é um ArrayList");
    }
}
