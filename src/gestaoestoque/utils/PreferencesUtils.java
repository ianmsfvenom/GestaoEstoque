package gestaoestoque.utils;

import java.util.prefs.Preferences;

import javax.crypto.SecretKey;

import gestaoestoque.Main;
import gestaoestoque.models.Usuario;

public class PreferencesUtils {
    private static final Preferences prefs = Preferences.userRoot().node(PreferencesUtils.class.getName());
    public static final String AES_KEY = "ZL=>(gDBrd7={lO97t2Ow?d4";
    
	public static boolean isAutenticado() { 
		try {
			String usernameCriptografado = prefs.get("nome", null);
			String senhaCriptografado = prefs.get("senha", null);
			
			if(usernameCriptografado == null || senhaCriptografado == null)
				return false;
			
			SecretKey key = CriptoUtils.loadKeyAES();
			String username = CriptoUtils.decryptAES(usernameCriptografado, key);
			String senha = CriptoUtils.decryptAES(senhaCriptografado, key);
			
			Usuario usuario = Usuario.consultarPeloLoginSenha(username, senha);
			
			return usuario != null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void carregarUsuarioAutenticado() {
		try {
			String usernameCriptografado = prefs.get("nome", null);
			String senhaCriptografado = prefs.get("senha", null);
			
			if(usernameCriptografado == null || senhaCriptografado == null)
				return;
			
			SecretKey key = CriptoUtils.loadKeyAES();
			String username = CriptoUtils.decryptAES(usernameCriptografado, key);
			String senha = CriptoUtils.decryptAES(senhaCriptografado, key);
			
			Usuario usuario = Usuario.consultarPeloLoginSenha(username, senha);
		
			if(usuario != null) 
				Main.usuarioAutenticado = usuario;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void removerAuth() {
		prefs.remove("nome");
		prefs.remove("senha");
	}
	
	public static void salvarAuth(String nome, String senha) {
		try {
			SecretKey key = CriptoUtils.loadKeyAES();
			String usernameCriptografado = CriptoUtils.encryptAES(nome, key);
			String senhaCriptografado = CriptoUtils.encryptAES(senha, key);
			
			prefs.put("nome", usernameCriptografado);
			prefs.put("senha", senhaCriptografado);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
} 
