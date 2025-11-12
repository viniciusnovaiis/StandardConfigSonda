package br.com.pdasolucoes.standardconfig.network;

import org.ksoap2.serialization.SoapObject;

import br.com.pdasolucoes.standardconfig.network.enums.MessageConfiguration;
import br.com.pdasolucoes.standardconfig.network.enums.RequestInfo;
import br.com.pdasolucoes.standardconfig.network.enums.RequestType;
import br.com.pdasolucoes.standardconfig.network.enums.TypeService;

public class TestRequest extends SoapRequestBase {
    @Override
    public SoapObject getBodySoap() {
        return null;
    }

    @Override
    protected RequestInfo getRequestInfo() {
        return new RequestInfo("reposicaoservice.asmx",
                "GetReposicao",
                RequestType.OnLine,
                -1,
                false,
                null,
                null,
                null,
                "http://tempuri.org/",
                null,
                TypeService.SOAP
        ,0);
    }

    @Override
    public void processResult(Object data) {

    }

    @Override
    public void processError(MessageConfiguration result) {

    }
}
