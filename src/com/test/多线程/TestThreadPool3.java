//package com.test.多线程;
//
//
//public void uploadingTest111(HttpServletRequest request,
//        HttpServletResponse response, HttpSession session)
//        throws IOException {
//    ExecutorService es = Executors.newCachedThreadPool();
//    Collection<UploadCallable> co = new ArrayList<UploadCallable>();
//    List<String> picsList = new ArrayList<String>();
//    try {
//        DiskFileItemFactory dfif = new DiskFileItemFactory();
//        ServletFileUpload sfu = new ServletFileUpload(dfif);
//        List<FileItem> items = sfu.parseRequest(request);
//        Iterator it = items.iterator();
//        while (it.hasNext()) {
//            FileItem fileItem = (FileItem) it.next();
//                if (fileItem.getName() != null) {
//                    InputStream is = fileItem.getInputStream();//你需要从request中获取到输入流再操作
//                    OutputStream fos = ******;//获得输出流d的操作
//                    UploadCallable uc = new UploadCallable(is, fos, tc);
//                    co.add(uc);
//                }
//            }
//        es.invokeAll(co);
//    } catch (Exception e) {
//        e.printStackTrace();
//    }
//}