package com.browxy.wrapper.fileUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MimeTypeUtil {
    private static final Map<String, List<String>> mimeTypes = new HashMap<>();
    
    static {
    	mimeTypes.put("application/andrew-inset", Arrays.asList("ez"));
    	mimeTypes.put("application/appinstaller", Arrays.asList("appinstaller"));
    	mimeTypes.put("application/applixware", Arrays.asList("aw"));
    	mimeTypes.put("application/appx", Arrays.asList("appx"));
    	mimeTypes.put("application/appxbundle", Arrays.asList("appxbundle"));
    	mimeTypes.put("application/atom+xml", Arrays.asList("atom"));
    	mimeTypes.put("application/atomcat+xml", Arrays.asList("atomcat"));
    	mimeTypes.put("application/atomdeleted+xml", Arrays.asList("atomdeleted"));
    	mimeTypes.put("application/atomsvc+xml", Arrays.asList("atomsvc"));
    	mimeTypes.put("application/atsc-dwd+xml", Arrays.asList("dwd"));
    	mimeTypes.put("application/atsc-held+xml", Arrays.asList("held"));
    	mimeTypes.put("application/atsc-rsat+xml", Arrays.asList("rsat"));
    	mimeTypes.put("application/automationml-aml+xml", Arrays.asList("aml"));
    	mimeTypes.put("application/automationml-amlx+zip", Arrays.asList("amlx"));
    	mimeTypes.put("application/bdoc", Arrays.asList("bdoc"));
    	mimeTypes.put("application/calendar+xml", Arrays.asList("xcs"));
    	mimeTypes.put("application/ccxml+xml", Arrays.asList("ccxml"));
    	mimeTypes.put("application/cdfx+xml", Arrays.asList("cdfx"));
    	mimeTypes.put("application/cdmi-capability", Arrays.asList("cdmia"));
    	mimeTypes.put("application/cdmi-container", Arrays.asList("cdmic"));
    	mimeTypes.put("application/cdmi-domain", Arrays.asList("cdmid"));
    	mimeTypes.put("application/cdmi-object", Arrays.asList("cdmio"));
    	mimeTypes.put("application/cdmi-queue", Arrays.asList("cdmiq"));
    	mimeTypes.put("application/cpl+xml", Arrays.asList("cpl"));
    	mimeTypes.put("application/cu-seeme", Arrays.asList("cu"));
    	mimeTypes.put("application/cwl", Arrays.asList("cwl"));
    	mimeTypes.put("application/dash+xml", Arrays.asList("mpd"));
    	mimeTypes.put("application/dash-patch+xml", Arrays.asList("mpp"));
    	mimeTypes.put("application/davmount+xml", Arrays.asList("davmount"));
    	mimeTypes.put("application/docbook+xml", Arrays.asList("dbk"));
    	mimeTypes.put("application/dssc+der", Arrays.asList("dssc"));
    	mimeTypes.put("application/dssc+xml", Arrays.asList("xdssc"));
    	mimeTypes.put("application/ecmascript", Arrays.asList("ecma"));
    	mimeTypes.put("application/emma+xml", Arrays.asList("emma"));
    	mimeTypes.put("application/emotionml+xml", Arrays.asList("emotionml"));
    	mimeTypes.put("application/epub+zip", Arrays.asList("epub"));
    	mimeTypes.put("application/exi", Arrays.asList("exi"));
    	mimeTypes.put("application/express", Arrays.asList("exp"));
    	mimeTypes.put("application/fdf", Arrays.asList("fdf"));
    	mimeTypes.put("application/fdt+xml", Arrays.asList("fdt"));
    	mimeTypes.put("application/font-tdpfr", Arrays.asList("pfr"));
    	mimeTypes.put("application/geo+json", Arrays.asList("geojson"));
    	mimeTypes.put("application/gml+xml", Arrays.asList("gml"));
    	mimeTypes.put("application/gpx+xml", Arrays.asList("gpx"));
    	mimeTypes.put("application/gxf", Arrays.asList("gxf"));
    	mimeTypes.put("application/gzip", Arrays.asList("gz"));
    	mimeTypes.put("application/hjson", Arrays.asList("hjson"));
    	mimeTypes.put("application/hyperstudio", Arrays.asList("stk"));
    	mimeTypes.put("application/inkml+xml", Arrays.asList("ink", "inkml"));
    	mimeTypes.put("application/ipfix", Arrays.asList("ipfix"));
    	mimeTypes.put("application/its+xml", Arrays.asList("its"));
    	mimeTypes.put("application/java-archive", Arrays.asList("jar", "war", "ear"));
    	mimeTypes.put("application/java-serialized-object", Arrays.asList("ser"));
    	mimeTypes.put("application/java-vm", Arrays.asList("class"));
    	mimeTypes.put("application/javascript", Arrays.asList("*js"));
    	mimeTypes.put("application/json", Arrays.asList("json", "map"));
    	mimeTypes.put("application/json5", Arrays.asList("json5"));
    	mimeTypes.put("application/jsonml+json", Arrays.asList("jsonml"));
    	mimeTypes.put("application/ld+json", Arrays.asList("jsonld"));
    	mimeTypes.put("application/lgr+xml", Arrays.asList("lgr"));
    	mimeTypes.put("application/lost+xml", Arrays.asList("lostxml"));
    	mimeTypes.put("application/mac-binhex40", Arrays.asList("hqx"));
    	mimeTypes.put("application/mac-compactpro", Arrays.asList("cpt"));
    	mimeTypes.put("application/mads+xml", Arrays.asList("mads"));
    	mimeTypes.put("application/manifest+json", Arrays.asList("webmanifest"));
    	mimeTypes.put("application/marc", Arrays.asList("mrc"));
    	mimeTypes.put("application/marcxml+xml", Arrays.asList("mrcx"));
    	mimeTypes.put("application/mathematica", Arrays.asList("ma", "nb", "mb"));
    	mimeTypes.put("application/mathml+xml", Arrays.asList("mathml"));
    	mimeTypes.put("application/mbox", Arrays.asList("mbox"));
    	mimeTypes.put("application/media-policy-dataset+xml", Arrays.asList("mpf"));
    	mimeTypes.put("application/mediaservercontrol+xml", Arrays.asList("mscml"));
    	mimeTypes.put("application/metalink+xml", Arrays.asList("metalink"));
    	mimeTypes.put("application/metalink4+xml", Arrays.asList("meta4"));
    	mimeTypes.put("application/mets+xml", Arrays.asList("mets"));
    	mimeTypes.put("application/mmt-aei+xml", Arrays.asList("maei"));
    	mimeTypes.put("application/mmt-usd+xml", Arrays.asList("musd"));
    	mimeTypes.put("application/mods+xml", Arrays.asList("mods"));
    	mimeTypes.put("application/mp21", Arrays.asList("m21", "mp21"));
    	mimeTypes.put("application/mp4", Arrays.asList("*mp4", "*mpg4", "mp4s", "m4p"));
    	mimeTypes.put("application/msix", Arrays.asList("msix"));
    	mimeTypes.put("application/msixbundle", Arrays.asList("msixbundle"));
    	mimeTypes.put("application/msword", Arrays.asList("doc", "dot"));
    	mimeTypes.put("application/mxf", Arrays.asList("mxf"));
    	mimeTypes.put("application/n-quads", Arrays.asList("nq"));
    	mimeTypes.put("application/n-triples", Arrays.asList("nt"));
    	mimeTypes.put("application/node", Arrays.asList("cjs"));
    	mimeTypes.put("application/octet-stream", Arrays.asList(
    	    "bin",
    	    "dms",
    	    "lrf",
    	    "mar",
    	    "so",
    	    "dist",
    	    "distz",
    	    "pkg",
    	    "bpk",
    	    "dump",
    	    "elc",
    	    "deploy",
    	    "exe",
    	    "dll",
    	    "deb",
    	    "dmg",
    	    "iso",
    	    "img",
    	    "msi",
    	    "msp",
    	    "msm",
    	    "buffer"
    	));
    	mimeTypes.put("application/oda", Arrays.asList("oda"));
    	mimeTypes.put("application/oebps-package+xml", Arrays.asList("opf"));
    	mimeTypes.put("application/ogg", Arrays.asList("ogx"));
    	mimeTypes.put("application/omdoc+xml", Arrays.asList("omdoc"));
    	mimeTypes.put("application/onenote", Arrays.asList("onetoc", "onetoc2", "onetmp", "onepkg"));
    	mimeTypes.put("application/oxps", Arrays.asList("oxps"));
    	mimeTypes.put("application/p2p-overlay+xml", Arrays.asList("relo"));
    	mimeTypes.put("application/patch-ops-error+xml", Arrays.asList("xer"));
    	mimeTypes.put("application/pdf", Arrays.asList("pdf"));
    	mimeTypes.put("application/pgp-encrypted", Arrays.asList("pgp"));
    	mimeTypes.put("application/pgp-keys", Arrays.asList("asc"));
    	mimeTypes.put("application/pgp-signature", Arrays.asList("sig", "*asc"));
    	mimeTypes.put("application/pics-rules", Arrays.asList("prf"));
    	mimeTypes.put("application/pkcs10", Arrays.asList("p10"));
    	mimeTypes.put("application/pkcs7-mime", Arrays.asList("p7m", "p7c"));
    	mimeTypes.put("application/pkcs7-signature", Arrays.asList("p7s"));
    	mimeTypes.put("application/pkcs8", Arrays.asList("p8"));
    	mimeTypes.put("application/pkix-attr-cert", Arrays.asList("ac"));
    	mimeTypes.put("application/pkix-cert", Arrays.asList("cer"));
    	mimeTypes.put("application/pkix-crl", Arrays.asList("crl"));
    	mimeTypes.put("application/pkix-pkipath", Arrays.asList("pkipath"));
    	mimeTypes.put("application/pkixcmp", Arrays.asList("pki"));
    	mimeTypes.put("application/pls+xml", Arrays.asList("pls"));
    	mimeTypes.put("application/postscript", Arrays.asList("ai", "eps", "ps"));
    	mimeTypes.put("application/provenance+xml", Arrays.asList("provx"));
    	mimeTypes.put("application/pskc+xml", Arrays.asList("pskcxml"));
    	mimeTypes.put("application/raml+yaml", Arrays.asList("raml"));
    	mimeTypes.put("application/rdf+xml", Arrays.asList("rdf", "owl"));
    	mimeTypes.put("application/reginfo+xml", Arrays.asList("rif"));
    	mimeTypes.put("application/relax-ng-compact-syntax", Arrays.asList("rnc"));
    	mimeTypes.put("application/resource-lists+xml", Arrays.asList("rl"));
    	mimeTypes.put("application/resource-lists-diff+xml", Arrays.asList("rld"));
    	mimeTypes.put("application/rls-services+xml", Arrays.asList("rs"));
    	mimeTypes.put("application/route-apd+xml", Arrays.asList("rapd"));
    	mimeTypes.put("application/route-s-tsid+xml", Arrays.asList("sls"));
    	mimeTypes.put("application/route-usd+xml", Arrays.asList("rusd"));
    	mimeTypes.put("application/rpki-ghostbusters", Arrays.asList("gbr"));
    	mimeTypes.put("application/rpki-manifest", Arrays.asList("mft"));
    	mimeTypes.put("application/rpki-roa", Arrays.asList("roa"));
    	mimeTypes.put("application/rsd+xml", Arrays.asList("rsd"));
    	mimeTypes.put("application/rss+xml", Arrays.asList("rss"));
    	mimeTypes.put("application/rtf", Arrays.asList("rtf"));
    	mimeTypes.put("application/sbml+xml", Arrays.asList("sbml"));
    	mimeTypes.put("application/scvp-cv-request", Arrays.asList("scq"));
    	mimeTypes.put("application/scvp-cv-response", Arrays.asList("scs"));
    	mimeTypes.put("application/scvp-vp-request", Arrays.asList("spq"));
    	mimeTypes.put("application/scvp-vp-response", Arrays.asList("spp"));
    	mimeTypes.put("application/sdp", Arrays.asList("sdp"));
    	mimeTypes.put("application/senml+xml", Arrays.asList("senmlx"));
    	mimeTypes.put("application/sensml+xml", Arrays.asList("sensmlx"));
    	mimeTypes.put("application/set-payment-initiation", Arrays.asList("setpay"));
    	mimeTypes.put("application/set-registration-initiation", Arrays.asList("setreg"));
    	mimeTypes.put("application/shf+xml", Arrays.asList("shf"));
    	mimeTypes.put("application/sieve", Arrays.asList("siv", "sieve"));
    	mimeTypes.put("application/smil+xml", Arrays.asList("smi", "smil"));
    	mimeTypes.put("application/sparql-query", Arrays.asList("rq"));
    	mimeTypes.put("application/sparql-results+xml", Arrays.asList("srx"));
    	mimeTypes.put("application/sql", Arrays.asList("sql"));
    	mimeTypes.put("application/srgs", Arrays.asList("gram"));
    	mimeTypes.put("application/srgs+xml", Arrays.asList("grxml"));
    	mimeTypes.put("application/sru+xml", Arrays.asList("sru"));
    	mimeTypes.put("application/ssdl+xml", Arrays.asList("ssdl"));
    	mimeTypes.put("application/ssml+xml", Arrays.asList("ssml"));
    	mimeTypes.put("application/swid+xml", Arrays.asList("swidtag"));
    	mimeTypes.put("application/tei+xml", Arrays.asList("tei", "teicorpus"));
    	mimeTypes.put("application/thraud+xml", Arrays.asList("tfi"));
    	mimeTypes.put("application/timestamped-data", Arrays.asList("tsd"));
    	mimeTypes.put("application/toml", Arrays.asList("toml"));
    	mimeTypes.put("application/trig", Arrays.asList("trig"));
    	mimeTypes.put("application/ttml+xml", Arrays.asList("ttml"));
    	mimeTypes.put("application/ubjson", Arrays.asList("ubj"));
    	mimeTypes.put("application/urc-ressheet+xml", Arrays.asList("rsheet"));
    	mimeTypes.put("application/urc-targetdesc+xml", Arrays.asList("td"));
    	mimeTypes.put("application/voicexml+xml", Arrays.asList("vxml"));
    	mimeTypes.put("application/wasm", Arrays.asList("wasm"));
    	mimeTypes.put("application/watcherinfo+xml", Arrays.asList("wif"));
    	mimeTypes.put("application/widget", Arrays.asList("wgt"));
    	mimeTypes.put("application/winhlp", Arrays.asList("hlp"));
    	mimeTypes.put("application/wsdl+xml", Arrays.asList("wsdl"));
    	mimeTypes.put("application/wspolicy+xml", Arrays.asList("wspolicy"));
    	mimeTypes.put("application/xaml+xml", Arrays.asList("xaml"));
    	mimeTypes.put("application/xcap-att+xml", Arrays.asList("xav"));
    	mimeTypes.put("application/xcap-caps+xml", Arrays.asList("xca"));
    	mimeTypes.put("application/xcap-diff+xml", Arrays.asList("xdf"));
    	mimeTypes.put("application/xcap-el+xml", Arrays.asList("xel"));
    	mimeTypes.put("application/xcap-ns+xml", Arrays.asList("xns"));
    	mimeTypes.put("application/xenc+xml", Arrays.asList("xenc"));
    	mimeTypes.put("application/xfdf", Arrays.asList("xfdf"));
    	mimeTypes.put("application/xhtml+xml", Arrays.asList("xhtml", "xht"));
    	mimeTypes.put("application/xliff+xml", Arrays.asList("xlf"));
    	mimeTypes.put("application/xml", Arrays.asList("xml", "xsl", "xsd", "rng"));
    	mimeTypes.put("application/xml-dtd", Arrays.asList("dtd"));
    	mimeTypes.put("application/xop+xml", Arrays.asList("xop"));
    	mimeTypes.put("application/xproc+xml", Arrays.asList("xpl"));
    	mimeTypes.put("application/xslt+xml", Arrays.asList("*xsl", "xslt"));
    	mimeTypes.put("application/xspf+xml", Arrays.asList("xspf"));
    	mimeTypes.put("application/xv+xml", Arrays.asList("mxml", "xhvml", "xvml", "xvm"));
    	mimeTypes.put("application/yang", Arrays.asList("yang"));
    	mimeTypes.put("application/yin+xml", Arrays.asList("yin"));
    	mimeTypes.put("application/zip", Arrays.asList("zip"));

    	mimeTypes.put("audio/3gpp", Arrays.asList("*3gpp"));
    	mimeTypes.put("audio/aac", Arrays.asList("adts", "aac"));
    	mimeTypes.put("audio/adpcm", Arrays.asList("adp"));
    	mimeTypes.put("audio/amr", Arrays.asList("amr"));
    	mimeTypes.put("audio/basic", Arrays.asList("au", "snd"));
    	mimeTypes.put("audio/midi", Arrays.asList("mid", "midi", "kar", "rmi"));
    	mimeTypes.put("audio/mobile-xmf", Arrays.asList("mxmf"));
    	mimeTypes.put("audio/mp3", Arrays.asList("*mp3"));
    	mimeTypes.put("audio/mp4", Arrays.asList("m4a", "mp4a"));
    	mimeTypes.put("audio/mpeg", Arrays.asList("mpga", "mp2", "mp2a", "mp3", "m2a", "m3a"));
    	mimeTypes.put("audio/ogg", Arrays.asList("oga", "ogg", "spx", "opus"));
    	mimeTypes.put("audio/s3m", Arrays.asList("s3m"));
    	mimeTypes.put("audio/silk", Arrays.asList("sil"));
    	mimeTypes.put("audio/wav", Arrays.asList("wav"));
    	mimeTypes.put("audio/wave", Arrays.asList("*wav"));
    	mimeTypes.put("audio/webm", Arrays.asList("weba"));
    	mimeTypes.put("audio/xm", Arrays.asList("xm"));

    	mimeTypes.put("font/collection", Arrays.asList("ttc"));
    	mimeTypes.put("font/otf", Arrays.asList("otf"));
    	mimeTypes.put("font/ttf", Arrays.asList("ttf"));
    	mimeTypes.put("font/woff", Arrays.asList("woff"));
    	mimeTypes.put("font/woff2", Arrays.asList("woff2"));

    	mimeTypes.put("image/aces", Arrays.asList("exr"));
    	mimeTypes.put("image/apng", Arrays.asList("apng"));
    	mimeTypes.put("image/avci", Arrays.asList("avci"));
    	mimeTypes.put("image/avcs", Arrays.asList("avcs"));
    	mimeTypes.put("image/avif", Arrays.asList("avif"));
    	mimeTypes.put("image/bmp", Arrays.asList("bmp", "dib"));
    	mimeTypes.put("image/cgm", Arrays.asList("cgm"));
    	mimeTypes.put("image/dicom-rle", Arrays.asList("drle"));
    	mimeTypes.put("image/dpx", Arrays.asList("dpx"));
    	mimeTypes.put("image/emf", Arrays.asList("emf"));
    	mimeTypes.put("image/fits", Arrays.asList("fits"));
    	mimeTypes.put("image/g3fax", Arrays.asList("g3"));
    	mimeTypes.put("image/gif", Arrays.asList("gif"));
    	mimeTypes.put("image/heic", Arrays.asList("heic"));
    	mimeTypes.put("image/heic-sequence", Arrays.asList("heics"));
    	mimeTypes.put("image/heif", Arrays.asList("heif"));
    	mimeTypes.put("image/heif-sequence", Arrays.asList("heifs"));
    	mimeTypes.put("image/hej2k", Arrays.asList("hej2"));
    	mimeTypes.put("image/hsj2", Arrays.asList("hsj2"));
    	mimeTypes.put("image/ief", Arrays.asList("ief"));
    	mimeTypes.put("image/jls", Arrays.asList("jls"));
    	mimeTypes.put("image/jp2", Arrays.asList("jp2", "jpg2"));
    	mimeTypes.put("image/jpeg", Arrays.asList("jpeg", "jpg", "jpe"));
    	mimeTypes.put("image/jph", Arrays.asList("jph"));
    	mimeTypes.put("image/jphc", Arrays.asList("jhc"));
    	mimeTypes.put("image/jpm", Arrays.asList("jpm", "jpgm"));
    	mimeTypes.put("image/jpx", Arrays.asList("jpx", "jpf"));
    	mimeTypes.put("image/jxl", Arrays.asList("jxl"));
    	mimeTypes.put("image/jxr", Arrays.asList("jxr"));
    	mimeTypes.put("image/jxra", Arrays.asList("jxra"));
    	mimeTypes.put("image/jxrs", Arrays.asList("jxrs"));
    	mimeTypes.put("image/jxs", Arrays.asList("jxs"));
    	mimeTypes.put("image/jxsc", Arrays.asList("jxsc"));
    	mimeTypes.put("image/jxsi", Arrays.asList("jxsi"));
    	mimeTypes.put("image/jxss", Arrays.asList("jxss"));
    	mimeTypes.put("image/ktx", Arrays.asList("ktx"));
    	mimeTypes.put("image/ktx2", Arrays.asList("ktx2"));
    	mimeTypes.put("image/png", Arrays.asList("png"));
    	mimeTypes.put("image/sgi", Arrays.asList("sgi"));
    	mimeTypes.put("image/svg+xml", Arrays.asList("svg", "svgz"));
    	mimeTypes.put("image/t38", Arrays.asList("t38"));
    	mimeTypes.put("image/tiff", Arrays.asList("tif", "tiff"));
    	mimeTypes.put("image/tiff-fx", Arrays.asList("tfx"));
    	mimeTypes.put("image/webp", Arrays.asList("webp"));
    	mimeTypes.put("image/wmf", Arrays.asList("wmf"));

    	mimeTypes.put("message/disposition-notification", Arrays.asList("disposition-notification"));
    	mimeTypes.put("message/global", Arrays.asList("u8msg"));
    	mimeTypes.put("message/global-delivery-status", Arrays.asList("u8dsn"));
    	mimeTypes.put("message/global-disposition-notification", Arrays.asList("u8mdn"));
    	mimeTypes.put("message/global-headers", Arrays.asList("u8hdr"));
    	mimeTypes.put("message/rfc822", Arrays.asList("eml", "mime"));

    	mimeTypes.put("model/3mf", Arrays.asList("3mf"));
    	mimeTypes.put("model/gltf+json", Arrays.asList("gltf"));
    	mimeTypes.put("model/gltf-binary", Arrays.asList("glb"));
    	mimeTypes.put("model/iges", Arrays.asList("igs", "iges"));
    	mimeTypes.put("model/jt", Arrays.asList("jt"));
    	mimeTypes.put("model/mesh", Arrays.asList("msh", "mesh", "silo"));
    	mimeTypes.put("model/mtl", Arrays.asList("mtl"));
    	mimeTypes.put("model/obj", Arrays.asList("obj"));
    	mimeTypes.put("model/prc", Arrays.asList("prc"));
    	mimeTypes.put("model/step+xml", Arrays.asList("stpx"));
    	mimeTypes.put("model/step+zip", Arrays.asList("stpz"));
    	mimeTypes.put("model/step-xml+zip", Arrays.asList("stpxz"));
    	mimeTypes.put("model/stl", Arrays.asList("stl"));
    	mimeTypes.put("model/u3d", Arrays.asList("u3d"));
    	mimeTypes.put("model/vrml", Arrays.asList("wrl", "vrml"));
    	mimeTypes.put("model/x3d+binary", Arrays.asList("*x3db", "x3dbz"));
    	mimeTypes.put("model/x3d+fastinfoset", Arrays.asList("x3db"));
    	mimeTypes.put("model/x3d+vrml", Arrays.asList("*x3dv", "x3dvz"));
    	mimeTypes.put("model/x3d+xml", Arrays.asList("x3d", "x3dz"));
    	mimeTypes.put("model/x3d-vrml", Arrays.asList("x3dv"));

    	mimeTypes.put("text/cache-manifest", Arrays.asList("appcache", "manifest"));
    	mimeTypes.put("text/calendar", Arrays.asList("ics", "ifb"));
    	mimeTypes.put("text/coffeescript", Arrays.asList("coffee", "litcoffee"));
    	mimeTypes.put("text/css", Arrays.asList("css"));
    	mimeTypes.put("text/csv", Arrays.asList("csv"));
    	mimeTypes.put("text/html", Arrays.asList("html", "htm", "shtml"));
    	mimeTypes.put("text/jade", Arrays.asList("jade"));
    	mimeTypes.put("text/javascript", Arrays.asList("js", "mjs"));
    	mimeTypes.put("text/jsx", Arrays.asList("jsx"));
    	mimeTypes.put("text/less", Arrays.asList("less"));
    	mimeTypes.put("text/markdown", Arrays.asList("md", "markdown"));
    	mimeTypes.put("text/mathml", Arrays.asList("mml"));
    	mimeTypes.put("text/mdx", Arrays.asList("mdx"));
    	mimeTypes.put("text/n3", Arrays.asList("n3"));
    	mimeTypes.put("text/plain", Arrays.asList("txt", "text", "conf", "def", "list", "log", "in", "ini"));
    	mimeTypes.put("text/richtext", Arrays.asList("rtx"));
    	mimeTypes.put("text/rtf", Arrays.asList("*rtf"));
    	mimeTypes.put("text/sgml", Arrays.asList("sgml", "sgm"));
    	mimeTypes.put("text/shex", Arrays.asList("shex"));
    	mimeTypes.put("text/slim", Arrays.asList("slim", "slm"));
    	mimeTypes.put("text/spdx", Arrays.asList("spdx"));
    	mimeTypes.put("text/stylus", Arrays.asList("stylus", "styl"));
    	mimeTypes.put("text/tab-separated-values", Arrays.asList("tsv"));
    	mimeTypes.put("text/troff", Arrays.asList("t", "tr", "roff", "man", "me", "ms"));
    	mimeTypes.put("text/turtle", Arrays.asList("ttl"));
    	mimeTypes.put("text/uri-list", Arrays.asList("uri", "uris", "urls"));
    	mimeTypes.put("text/vcard", Arrays.asList("vcard"));
    	mimeTypes.put("text/vtt", Arrays.asList("vtt"));
    	mimeTypes.put("text/wgsl", Arrays.asList("wgsl"));
    	mimeTypes.put("text/xml", Arrays.asList("*xml"));
    	mimeTypes.put("text/yaml", Arrays.asList("yaml", "yml"));

    	mimeTypes.put("video/3gpp", Arrays.asList("3gp", "3gpp"));
    	mimeTypes.put("video/3gpp2", Arrays.asList("3g2"));
    	mimeTypes.put("video/h261", Arrays.asList("h261"));
    	mimeTypes.put("video/h263", Arrays.asList("h263"));
    	mimeTypes.put("video/h264", Arrays.asList("h264"));
    	mimeTypes.put("video/iso.segment", Arrays.asList("m4s"));
    	mimeTypes.put("video/jpeg", Arrays.asList("jpgv"));
    	mimeTypes.put("video/jpm", Arrays.asList("*jpm", "*jpgm"));
    	mimeTypes.put("video/mj2", Arrays.asList("mj2", "mjp2"));
    	mimeTypes.put("video/mp2t", Arrays.asList("ts", "m2t", "m2ts", "mts"));
    	mimeTypes.put("video/mp4", Arrays.asList("mp4", "mp4v", "mpg4"));
    	mimeTypes.put("video/mpeg", Arrays.asList("mpeg", "mpg", "mpe", "m1v", "m2v"));
    	mimeTypes.put("video/ogg", Arrays.asList("ogv"));
    	mimeTypes.put("video/quicktime", Arrays.asList("qt", "mov"));
    	mimeTypes.put("video/webm", Arrays.asList("webm"));


    }
  
   private static String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            return fileName.substring(index + 1).toLowerCase(); 
        }
        return ""; 
    }
   

    public static String getMimeTypeByFileName(String fileName) {
        String extension = getFileExtension(fileName);
        for (Map.Entry<String, List<String>> entry : mimeTypes.entrySet()) {
            if (entry.getValue().contains(extension)) {
                return entry.getKey(); 
            }
        }
        return "application/octet-stream"; 
    }
}
