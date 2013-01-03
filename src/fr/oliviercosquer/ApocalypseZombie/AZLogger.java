package fr.oliviercosquer.ApocalypseZombie;

import fr.oliviercosquer.ApocalypseZombie.ApocalypseZombie;
import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;

public class AZLogger {

	private ApocalypseZombie plugin;
	private Logger log;

	public AZLogger(ApocalypseZombie plugin){
		this.plugin = plugin;
		this.log = Logger.getLogger("Minecraft");
	}

	private String FormatMessage(String message){
		PluginDescriptionFile pdFile = plugin.getDescription();
		return "[" + pdFile.getName() + "] " + message;
	}

	public void infoMSG(String message){
		this.log.info(this.FormatMessage(message));
	}

	public void warningMSG(String message){
		this.log.warning(this.FormatMessage(message));
	}

	public void severeMSG(String message){
		this.log.severe(this.FormatMessage(message));
	}

}