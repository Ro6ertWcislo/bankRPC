package sr.grpc.gen;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.2.0)",
    comments = "Source: currency.proto")
public final class CurrencyProviderGrpc {

  private CurrencyProviderGrpc() {}

  public static final String SERVICE_NAME = "CurrencyProvider";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<sr.grpc.gen.Currency,
      sr.grpc.gen.ExchangeRate> METHOD_GET_EXCHANGE_RATES =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "CurrencyProvider", "getExchangeRates"),
          io.grpc.protobuf.ProtoUtils.marshaller(sr.grpc.gen.Currency.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(sr.grpc.gen.ExchangeRate.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CurrencyProviderStub newStub(io.grpc.Channel channel) {
    return new CurrencyProviderStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CurrencyProviderBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CurrencyProviderBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static CurrencyProviderFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CurrencyProviderFutureStub(channel);
  }

  /**
   */
  public static abstract class CurrencyProviderImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<sr.grpc.gen.Currency> getExchangeRates(
        io.grpc.stub.StreamObserver<sr.grpc.gen.ExchangeRate> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_GET_EXCHANGE_RATES, responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_GET_EXCHANGE_RATES,
            asyncBidiStreamingCall(
              new MethodHandlers<
                sr.grpc.gen.Currency,
                sr.grpc.gen.ExchangeRate>(
                  this, METHODID_GET_EXCHANGE_RATES)))
          .build();
    }
  }

  /**
   */
  public static final class CurrencyProviderStub extends io.grpc.stub.AbstractStub<CurrencyProviderStub> {
    private CurrencyProviderStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CurrencyProviderStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyProviderStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CurrencyProviderStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<sr.grpc.gen.Currency> getExchangeRates(
        io.grpc.stub.StreamObserver<sr.grpc.gen.ExchangeRate> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_GET_EXCHANGE_RATES, getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class CurrencyProviderBlockingStub extends io.grpc.stub.AbstractStub<CurrencyProviderBlockingStub> {
    private CurrencyProviderBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CurrencyProviderBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyProviderBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CurrencyProviderBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class CurrencyProviderFutureStub extends io.grpc.stub.AbstractStub<CurrencyProviderFutureStub> {
    private CurrencyProviderFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CurrencyProviderFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyProviderFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CurrencyProviderFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_GET_EXCHANGE_RATES = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CurrencyProviderImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CurrencyProviderImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_EXCHANGE_RATES:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.getExchangeRates(
              (io.grpc.stub.StreamObserver<sr.grpc.gen.ExchangeRate>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class CurrencyProviderDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return sr.grpc.gen.CurrencyProto.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CurrencyProviderGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CurrencyProviderDescriptorSupplier())
              .addMethod(METHOD_GET_EXCHANGE_RATES)
              .build();
        }
      }
    }
    return result;
  }
}
