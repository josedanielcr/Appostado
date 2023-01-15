package cr.ac.cenfotec.appostado.service;

import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LigaService {

    public LigaService() {}

    public String processDescripcion(String login, String descOri) {
        String descNew = login + "//" + descOri;
        return descNew;
    }

    public String dismantleDescripcion(String descOri) {
        String descNew;
        Pattern p = Pattern.compile("//");
        String[] DescSplit = p.split(descOri, 2);
        descNew = DescSplit[1];
        return descNew;
    }

    public String getLigaOwner(String desc) {
        String owner;
        Pattern p = Pattern.compile("//");
        String[] DescSplit = p.split(desc, 2);
        owner = DescSplit[0];

        return owner;
    }
}
