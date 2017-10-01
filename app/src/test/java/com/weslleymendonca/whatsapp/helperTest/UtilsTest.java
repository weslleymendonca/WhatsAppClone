package com.weslleymendonca.whatsapp.helperTest;

import com.weslleymendonca.whatsapp.helper.Utils;

import org.junit.Test;
import org.junit.runners.model.TestClass;

import static org.junit.Assert.*;

/**
 * Created by weslley on 06/09/2017.
 */

public class UtilsTest{

    @Test
    public void verificaSeOsCamposEstaoVaziosTest(){
        String email = "";
        String senha = "";

        Utils utils = new Utils();
        assertFalse(utils.verificaCamposLogin(email, senha));

    }



}
