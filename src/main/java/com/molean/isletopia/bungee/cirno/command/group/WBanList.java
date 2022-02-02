package com.molean.isletopia.bungee.cirno.command.group;

import com.molean.isletopia.bungee.cirno.BotCommandExecutor;
import com.molean.isletopia.bungee.cirno.CirnoUtils;
import com.molean.isletopia.bungee.cirno.CommandHandler;
import net.mamoe.mirai.contact.file.AbsoluteFolder;
import net.mamoe.mirai.utils.ExternalResource;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WBanList implements BotCommandExecutor {
    public WBanList() {
        CommandHandler.setExecutor("wbanlist", this);
    }

    @Override
    public String execute(long id, List<String> args) throws Exception {
        String join = String.join("\n", WBan.stringSet);
        String path = "屏蔽词" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss")) + ".txt";
        AbsoluteFolder folder = CirnoUtils.getGameGroup().getFiles().getRoot().resolveFolder("失效文件");
        assert folder != null;
        folder.uploadNewFile(path, ExternalResource.create(join.getBytes(StandardCharsets.UTF_8), "txt"));
        return "尝试上传到群文件";
    }

}
